package org.example.testcodesample.user.service;

import java.time.Clock;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.example.testcodesample.common.domain.exception.CertificationCodeNotMatchedException;
import org.example.testcodesample.common.domain.exception.ResourceNotFoundException;
import org.example.testcodesample.user.domain.UserCreate;
import org.example.testcodesample.user.domain.UserStatus;
import org.example.testcodesample.user.domain.UserUpdate;
import org.example.testcodesample.user.infrastructure.UserEntity;
import org.example.testcodesample.user.service.port.UserRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JavaMailSender mailSender;

    //find는 optional을 반환한다는 의미
    public Optional<UserEntity> findById(long id) {
        return userRepository.findByIdAndStatus(id, UserStatus.ACTIVE);
    }

    public UserEntity getByEmail(String email) {
        return userRepository.findByEmailAndStatus(email, UserStatus.ACTIVE)
            .orElseThrow(() -> new ResourceNotFoundException("Users", email));
    }

    //get은 데이터가 없으면 에러를 던진다는 의미가 내포되어 있다.
    public UserEntity getById(long id) {
        return userRepository.findByIdAndStatus(id, UserStatus.ACTIVE)
            .orElseThrow(() -> new ResourceNotFoundException("Users", id));
    }

    //UserService 자체가 User에 대한 책임을 지고 있기 때문에 create만 적어줘도 됨
    @Transactional
    public UserEntity create(UserCreate userCreate) {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(userCreate.getEmail());
        userEntity.setNickname(userCreate.getNickname());
        userEntity.setAddress(userCreate.getAddress());
        userEntity.setStatus(UserStatus.PENDING);
        userEntity.setCertificationCode(UUID.randomUUID().toString());
        userEntity = userRepository.save(userEntity);
        String certificationUrl = generateCertificationUrl(userEntity);
        sendCertificationEmail(userCreate.getEmail(), certificationUrl);
        return userEntity;
    }

    //create와 마찬가지고 UserService 자체가 User에 대한 책임을 가지고 있음 User 생략
    @Transactional
    public UserEntity update(long id, UserUpdate userUpdate) {
        UserEntity userEntity = getById(id);
        userEntity.setNickname(userUpdate.getNickname());
        userEntity.setAddress(userUpdate.getAddress());
        userEntity = userRepository.save(userEntity);
        return userEntity;
    }

    @Transactional
    public void login(long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Users", id));
        userEntity.setLastLoginAt(Clock.systemUTC().millis());
    }

    @Transactional
    public void verifyEmail(long id, String certificationCode) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Users", id));
        if (!certificationCode.equals(userEntity.getCertificationCode())) {
            throw new CertificationCodeNotMatchedException();
        }
        userEntity.setStatus(UserStatus.ACTIVE);
    }

    private void sendCertificationEmail(String email, String certificationUrl) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Please certify your email address");
        message.setText("Please click the following link to certify your email address: " + certificationUrl);
        mailSender.send(message);
    }

    private String generateCertificationUrl(UserEntity userEntity) {
        return "http://localhost:8080/api/users/" + userEntity.getId() + "/verify?certificationCode=" + userEntity.getCertificationCode();
    }
}
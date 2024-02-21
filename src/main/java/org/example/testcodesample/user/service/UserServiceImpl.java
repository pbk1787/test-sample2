package org.example.testcodesample.user.service;

import lombok.RequiredArgsConstructor;
import org.example.testcodesample.common.domain.exception.ResourceNotFoundException;
import org.example.testcodesample.common.service.port.ClockHolder;
import org.example.testcodesample.common.service.port.UuidHolder;
import org.example.testcodesample.user.controller.port.UserService;
import org.example.testcodesample.user.domain.User;
import org.example.testcodesample.user.domain.UserCreate;
import org.example.testcodesample.user.domain.UserStatus;
import org.example.testcodesample.user.domain.UserUpdate;
import org.example.testcodesample.user.service.port.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CertificationService certificationService;
    private final UuidHolder uuidHolder;
    private final ClockHolder clockHolder;

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmailAndStatus(email, UserStatus.ACTIVE)
            .orElseThrow(() -> new ResourceNotFoundException("Users", email));
    }

    //get은 데이터가 없으면 에러를 던진다는 의미가 내포되어 있다.
    @Override
    public User getById(long id) {
        return userRepository.findByIdAndStatus(id, UserStatus.ACTIVE)
            .orElseThrow(() -> new ResourceNotFoundException("Users", id));
    }

    //UserService 자체가 User에 대한 책임을 지고 있기 때문에 create만 적어줘도 됨
    @Override
    @Transactional
    public User create(UserCreate userCreate) {
        User user = User.from(userCreate, uuidHolder);
        user = userRepository.save(user);
        certificationService.send(userCreate.getEmail(), user.getId(), user.getCertificationCode());
        return user;
    }

    //create와 마찬가지고 UserService 자체가 User에 대한 책임을 가지고 있음 User 생략
    @Override
    @Transactional
    public User update(long id, UserUpdate userUpdate) {
        User user = getById(id);
        user = user.update(userUpdate);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void login(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Users", id));
        user = user.login(clockHolder);
        //도메인과 영속성 객체를 분리함으로써 JPA 의존성이 끊어지게 되어 변경된 Entity를 감지할 수 없음
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void verifyEmail(long id, String certificationCode) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Users", id));
        user = user.certificate(certificationCode);
        userRepository.save(user);
    }

}
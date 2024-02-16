package org.example.testcodesample.user.domain;

import lombok.Builder;
import lombok.Getter;
import org.example.testcodesample.common.domain.exception.CertificationCodeNotMatchedException;
import org.example.testcodesample.common.service.port.ClockHolder;
import org.example.testcodesample.common.service.port.UuidHolder;

@Getter
public class User {

    private final Long id;
    private final String email;
    private final String nickname;
    private final String address;
    private final String certificationCode;
    private final UserStatus status;
    private final Long lastLoginAt;

    @Builder
    public User(Long id, String email, String nickname, String address, String certificationCode, UserStatus status, Long lastLoginAt) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.address = address;
        this.certificationCode = certificationCode;
        this.status = status;
        this.lastLoginAt = lastLoginAt;
    }

    public static User from(UserCreate userCreate, UuidHolder uuidHolder) {
        return User.builder()
            .email(userCreate.getEmail())
            .nickname(userCreate.getNickname())
            .address(userCreate.getAddress())
            .status(UserStatus.PENDING)
            .certificationCode(uuidHolder.random())
            .build();
    }

    public User update(UserUpdate userUpdate) {
        userUpdate.getAddress();
        userUpdate.getNickname();
        return User.builder()
            .id(id)
            .email(email)
            .nickname(userUpdate.getNickname())
            .address(userUpdate.getAddress())
            .status(status)
            .certificationCode(certificationCode)
            .lastLoginAt(lastLoginAt)
            .build();
    }

    /**
     * 로그인
     *
     * @return
     */
    public User login(ClockHolder clockHolder) {
        return User.builder()
            .id(id)
            .email(email)
            .nickname(nickname)
            .address(address)
            .status(status)
            .certificationCode(certificationCode)
            .lastLoginAt(clockHolder.millis())
            .build();
    }

    /**
     * 이메일 인증
     *
     * @return
     */
    public User certificate(String certificationCode) {
        if (!this.certificationCode.equals(certificationCode)) {
            throw new CertificationCodeNotMatchedException();
        }

        return User.builder()
            .id(id)
            .email(email)
            .nickname(nickname)
            .address(address)
            .status(UserStatus.ACTIVE)
            .certificationCode(certificationCode)
            .lastLoginAt(lastLoginAt)
            .build();
    }
}

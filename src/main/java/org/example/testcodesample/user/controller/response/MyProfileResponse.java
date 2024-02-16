package org.example.testcodesample.user.controller.response;

import lombok.Builder;
import lombok.Getter;
import org.example.testcodesample.user.domain.User;
import org.example.testcodesample.user.domain.UserStatus;

@Getter
public class MyProfileResponse {

    private Long id;
    private String email;
    private String nickname;
    private String address;
    private UserStatus status;
    private Long lastLoginAt;

    @Builder
    public MyProfileResponse(Long id, String email, String nickname, String address, UserStatus status, Long lastLoginAt) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.address = address;
        this.status = status;
        this.lastLoginAt = lastLoginAt;
    }

    public static MyProfileResponse from(User user) {
        return MyProfileResponse.builder()
            .id(user.getId())
            .email(user.getEmail())
            .nickname(user.getNickname())
            .status(user.getStatus())
            .address(user.getAddress())
            .lastLoginAt(user.getLastLoginAt())
            .build();
    }
}

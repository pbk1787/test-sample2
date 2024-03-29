package org.example.testcodesample.user.controller.response;

import lombok.Builder;
import lombok.Getter;
import org.example.testcodesample.user.domain.User;
import org.example.testcodesample.user.domain.UserStatus;

@Getter
public class UserResponse {

    private Long id;
    private String email;
    private String nickname;
    private UserStatus status;
    private Long lastLoginAt;

    @Builder
    public UserResponse(Long id, String email, String nickname, UserStatus status, Long lastLoginAt) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.status = status;
        this.lastLoginAt = lastLoginAt;
    }

    public static UserResponse from(User user) {
        return UserResponse.builder()
            .id(user.getId())
            .email(user.getEmail())
            .nickname(user.getNickname())
            .status(user.getStatus())
            .lastLoginAt(user.getLastLoginAt())
            .build();
    }

}

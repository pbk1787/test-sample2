package org.example.testcodesample.user.controller.response;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.example.testcodesample.user.domain.User;
import org.example.testcodesample.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

class UserResponseTest {

    @Test
    void User로_응답을_생성할_수_있다() {
        //given
        User user = User.builder()
            .id(1L)
            .email("test@test.com")
            .nickname("test")
            .address("Seoul")
            .status(UserStatus.ACTIVE)
            .lastLoginAt(100L)
            .certificationCode("aaa-aaa")
            .build();

        //when
        UserResponse userResponse = UserResponse.from(user);

        //then
        assertThat(userResponse.getId()).isEqualTo(1);
        assertThat(userResponse.getEmail()).isEqualTo("test@test.com");
        assertThat(userResponse.getNickname()).isEqualTo("test");
        assertThat(userResponse.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(userResponse.getLastLoginAt()).isEqualTo(100L);
    }

}
package org.example.testcodesample.user.controller.response;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.example.testcodesample.user.domain.User;
import org.example.testcodesample.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

class MyProfileResponseTest {

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
        MyProfileResponse myProfileResponse = MyProfileResponse.from(user);

        //then
        assertThat(myProfileResponse.getId()).isEqualTo(1);
        assertThat(myProfileResponse.getEmail()).isEqualTo("test@test.com");
        assertThat(myProfileResponse.getAddress()).isEqualTo("Seoul");
        assertThat(myProfileResponse.getNickname()).isEqualTo("test");
        assertThat(myProfileResponse.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(myProfileResponse.getLastLoginAt()).isEqualTo(100L);
    }
}
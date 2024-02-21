package org.example.testcodesample.user.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.example.testcodesample.mock.TestContainer;
import org.example.testcodesample.user.controller.response.UserResponse;
import org.example.testcodesample.user.domain.UserCreate;
import org.example.testcodesample.user.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

class UserCreateControllerTest {

    @Test
    void 사용자는_회원가입을_할_수있고_회원가입된_사용자는_PENDING_상태이다() {
        //given
        TestContainer testContainer = TestContainer.builder()
            .uuidHolder(() -> "1")
            .build();

        //when
        ResponseEntity<UserResponse> result = testContainer.userCreateController
            .createUser(UserCreate.builder()
                .email("test@test.com")
                .nickname("test")
                .address("gangnam")
                .build());

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1);
        assertThat(result.getBody().getEmail()).isEqualTo("test@test.com");
        assertThat(result.getBody().getNickname()).isEqualTo("test");
        assertThat(result.getBody().getLastLoginAt()).isNull();
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(testContainer.userRepository.getById(1).getCertificationCode()).isEqualTo("1");

    }

}
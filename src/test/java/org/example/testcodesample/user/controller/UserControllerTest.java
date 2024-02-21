package org.example.testcodesample.user.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.example.testcodesample.common.domain.exception.CertificationCodeNotMatchedException;
import org.example.testcodesample.common.domain.exception.ResourceNotFoundException;
import org.example.testcodesample.mock.TestContainer;
import org.example.testcodesample.user.controller.response.MyProfileResponse;
import org.example.testcodesample.user.controller.response.UserResponse;
import org.example.testcodesample.user.domain.User;
import org.example.testcodesample.user.domain.UserStatus;
import org.example.testcodesample.user.domain.UserUpdate;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

class UserControllerTest {

    @Test
    void 사용자는_특정_유저의_개인정보는_소거된_정보를_응답_받을_수_있다() {
        //given
        TestContainer testContainer = TestContainer.builder()
            .build();

        testContainer.userRepository.save(User.builder()
            .id(1L)
            .email("test@test.com")
            .nickname("test")
            .address("Seoul")
            .certificationCode("aaaaa-aaaa-aaaaa")
            .status(UserStatus.ACTIVE)
            .lastLoginAt(0L)
            .build());

        //when
        ResponseEntity<UserResponse> result = testContainer.userController
            .getById(1);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1);
        assertThat(result.getBody().getEmail()).isEqualTo("test@test.com");
        assertThat(result.getBody().getNickname()).isEqualTo("test");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void 존재하지_않는_유저의_아이디로_api_호출할_경우_404_응답을_받는다() {
        //given
        TestContainer testContainer = TestContainer.builder()
            .build();
        //when
        //then
        assertThatThrownBy(() -> testContainer.userController
            .getById(1))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void 사용자는_인증_코드로_계정을_활성화_시킬_수_있다() {
        //given
        TestContainer testContainer = TestContainer.builder()
            .build();

        testContainer.userRepository.save(User.builder()
            .id(1L)
            .email("test@test.com")
            .nickname("test")
            .address("Seoul")
            .certificationCode("aaaaa-aaaa-aaaaa")
            .status(UserStatus.PENDING)
            .lastLoginAt(0L)
            .build());

        //when
        ResponseEntity<Void> result = testContainer.userController
            .verifyEmail(1, "aaaaa-aaaa-aaaaa");

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(302));
        assertThat(testContainer.userRepository.getById(1).getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void 사용자는_인증_코드가_일치하지_않을_경우_권한_없음_에러를_내려준다() {
        //given
        TestContainer testContainer = TestContainer.builder()
            .build();

        testContainer.userRepository.save(User.builder()
            .id(1L)
            .email("test@test.com")
            .nickname("test")
            .address("Seoul")
            .certificationCode("aaaaa-aaaa-aaaaa")
            .status(UserStatus.PENDING)
            .lastLoginAt(0L)
            .build());

        //when
        //then
        assertThatThrownBy(() -> testContainer.userController
            .verifyEmail(1, "aaaaa-aaaa-aaabb"))
            .isInstanceOf(CertificationCodeNotMatchedException.class);
    }

    @Test
    void 사용자는_내_정보를_불러올_때_개인정보인_주소도_갖고_올_수_있다() {
        //given
        TestContainer testContainer = TestContainer.builder()
            .clockHolder(() -> 1678530673958L)
            .build();

        testContainer.userRepository.save(User.builder()
            .id(1L)
            .email("test@test.com")
            .nickname("test")
            .address("Seoul")
            .certificationCode("aaaaa-aaaa-aaaaa")
            .status(UserStatus.ACTIVE)
            .lastLoginAt(0L)
            .build());

        //when
        ResponseEntity<MyProfileResponse> result = testContainer.userController
            .getMyInfo("test@test.com");

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1);
        assertThat(result.getBody().getEmail()).isEqualTo("test@test.com");
        assertThat(result.getBody().getNickname()).isEqualTo("test");
        assertThat(result.getBody().getAddress()).isEqualTo("Seoul");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(result.getBody().getLastLoginAt()).isEqualTo(1678530673958L);
    }

    @Test
    void 사용자는_내_정보를_수정할_수_있다() {
        //given
        TestContainer testContainer = TestContainer.builder()
            .build();

        testContainer.userRepository.save(User.builder()
            .id(1L)
            .email("test@test.com")
            .nickname("test")
            .address("Seoul")
            .certificationCode("aaaaa-aaaa-aaaaa")
            .status(UserStatus.ACTIVE)
            .lastLoginAt(0L)
            .build());

        //when
        ResponseEntity<MyProfileResponse> result = testContainer.userController
            .updateMyInfo("test@test.com", UserUpdate.builder()
                .nickname("김이박")
                .address("Busan")
                .build());

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1);
        assertThat(result.getBody().getEmail()).isEqualTo("test@test.com");
        assertThat(result.getBody().getNickname()).isEqualTo("김이박");
        assertThat(result.getBody().getAddress()).isEqualTo("Busan");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
    }


}
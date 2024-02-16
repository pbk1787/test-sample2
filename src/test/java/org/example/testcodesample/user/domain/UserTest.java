package org.example.testcodesample.user.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import org.example.testcodesample.common.domain.exception.CertificationCodeNotMatchedException;
import org.example.testcodesample.mock.TestClockHolder;
import org.example.testcodesample.mock.TestUuidHolder;
import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    void User는_UserCreate_객체로_생성할_수_있다() {
        //given
        UserCreate userCreate = UserCreate.builder()
            .email("test@test.com")
            .address("Seoul")
            .nickname("test")
            .build();

        //when
        User user = User.from(userCreate, new TestUuidHolder("aaa-aaa"));

        //then
        assertThat(user.getId()).isNull();
        assertThat(user.getEmail()).isEqualTo("test@test.com");
        assertThat(user.getAddress()).isEqualTo("Seoul");
        assertThat(user.getNickname()).isEqualTo("test");
        assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(user.getCertificationCode()).isEqualTo("aaa-aaa");

    }

    @Test
    void User는_UserUpdate_객체로_데이터를_업데이트_할_수_있다() {
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

        UserUpdate userUpdate = UserUpdate.builder()
            .nickname("ghghgh")
            .address("incheon")
            .build();

        //when
        User update = user.update(userUpdate);

        //then
        assertThat(update.getId()).isEqualTo(1L);
        assertThat(update.getEmail()).isEqualTo("test@test.com");
        assertThat(update.getAddress()).isEqualTo("incheon");
        assertThat(update.getNickname()).isEqualTo("ghghgh");
        assertThat(update.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(update.getCertificationCode()).isEqualTo("aaa-aaa");
        assertThat(update.getLastLoginAt()).isEqualTo(100L);
    }

    @Test
    void User는_로그인을_할_수_있고_로그인시_마지막_로그인_시간이_변경된다() {
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
        user = user.login(new TestClockHolder(1678530673958L));

        //then
        assertThat(user.getLastLoginAt()).isEqualTo(1678530673958L);
    }

    @Test
    void User는_유효한_인증_코드로_계정을_활성화_할_수_있다() {
        //given
        User user = User.builder()
            .id(1L)
            .email("test@test.com")
            .nickname("test")
            .address("Seoul")
            .status(UserStatus.PENDING)
            .lastLoginAt(100L)
            .certificationCode("aaa-aaa")
            .build();

        //when
        user = user.certificate("aaa-aaa");

        //then
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void User는_잘못된_인증_코드로_계정을_활성화_하려하면_에러를_던지다() {
        //given
        User user = User.builder()
            .id(1L)
            .email("test@test.com")
            .nickname("test")
            .address("Seoul")
            .status(UserStatus.PENDING)
            .lastLoginAt(100L)
            .certificationCode("aaa-aaa")
            .build();

        //when
        //then
        assertThatThrownBy(() -> user.certificate("ddddd"))
            .isInstanceOf(CertificationCodeNotMatchedException.class);
    }

}
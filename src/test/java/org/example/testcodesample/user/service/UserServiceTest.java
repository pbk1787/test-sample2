package org.example.testcodesample.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.example.testcodesample.common.domain.exception.CertificationCodeNotMatchedException;
import org.example.testcodesample.common.domain.exception.ResourceNotFoundException;
import org.example.testcodesample.mock.FakeMailSender;
import org.example.testcodesample.mock.FakeUserRepository;
import org.example.testcodesample.mock.TestClockHolder;
import org.example.testcodesample.mock.TestUuidHolder;
import org.example.testcodesample.user.domain.User;
import org.example.testcodesample.user.domain.UserCreate;
import org.example.testcodesample.user.domain.UserStatus;
import org.example.testcodesample.user.domain.UserUpdate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserServiceTest {

    private UserServiceImpl userService;

    @BeforeEach
    void init() {
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        FakeMailSender fakeMailSender = new FakeMailSender();
        this.userService = new UserServiceImpl(
            fakeUserRepository,
            new CertificationService(fakeMailSender),
            new TestUuidHolder("aaaaa-aaaa-aaaaa"),
            new TestClockHolder(1678530673958L));

        fakeUserRepository.save(User.builder()
            .id(1L)
            .email("test@test.com")
            .nickname("test")
            .address("Seoul")
            .certificationCode("aaaaa-aaaa-aaaaa")
            .status(UserStatus.ACTIVE)
            .lastLoginAt(0L)
            .build());

        fakeUserRepository.save(User.builder()
            .id(2L)
            .email("test2@test.com")
            .nickname("test2")
            .address("Seoul")
            .certificationCode("bbbbb-bbbb-bbbb")
            .status(UserStatus.PENDING)
            .lastLoginAt(0L)
            .build());
    }

    @Test
    void getByEmail은_Active_상태인_유저를_찾아올_수_있다() {
        //given
        String email = "test@test.com";

        //when
        User result = userService.getByEmail(email);

        //then
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void getByEmail은_Pending_상태인_유저를_찾아올_수_없다() {
        //given
        String email = "test2@test.com";

        //when
        //then
        assertThatThrownBy(() -> {
            User result = userService.getByEmail(email);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getById는_Active_상태인_유저를_찾아올_수_있다() {
        //given
        //when
        User result = userService.getById(1);

        //then
        assertThat(result.getNickname()).isEqualTo("test");
    }

    @Test
    void getById는_Pending_상태인_유저를_찾아올_수_없다() {
        //given
        String email = "test2@test.com";

        //when
        //then
        assertThatThrownBy(() -> {
            User result = userService.getById(2);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void UserCreateDto를_이용하여_유저를_생성할_수_있다() {
        //given
        UserCreate userCreate = UserCreate.builder()
            .email("test3@test.com")
            .address("Seoul")
            .nickname("kangnam")
            .build();

        //when
        User result = userService.create(userCreate);

        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(result.getCertificationCode()).isEqualTo("aaaaa-aaaa-aaaaa");
    }

    @Test
    void UserUpdateDto를_이용하여_유저를_수정할_수_있다() {
        //given
        UserUpdate updateDto = UserUpdate.builder()
            .address("Incheon")
            .nickname("sea")
            .build();

        //when
        User update = userService.update(1, updateDto);

        //then
        assertThat(update.getId()).isNotNull();
        assertThat(update.getAddress()).isEqualTo("Incheon");
        assertThat(update.getNickname()).isEqualTo("sea");
    }

    @Test
    void user를_로그인_시키면_마지막_로그인_시간이_변경된다() {
        //given
        //when
        userService.login(1);

        //then
        User user = userService.getById(1);
        assertThat(user.getLastLoginAt()).isEqualTo(1678530673958L);
    }

    @Test
    void PENDING_상태의_사용자는_인증_코드로_ACTIVE_시킬_수_있다() {
        //given
        //when
        userService.verifyEmail(2, "bbbbb-bbbb-bbbb");

        //then
        User user = userService.getById(1);
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void PENDING_상태의_사용자는_잘못된_인증_코드를_받으면_에러를_던진다() {
        //given
        //when
        //then
        assertThatThrownBy(() -> {
            userService.verifyEmail(2, "bbbbb-bbbb-2222");
        }).isInstanceOf(CertificationCodeNotMatchedException.class);
    }

}
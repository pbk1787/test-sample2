package org.example.testcodesample.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import org.example.testcodesample.exception.CertificationCodeNotMatchedException;
import org.example.testcodesample.exception.ResourceNotFoundException;
import org.example.testcodesample.model.UserStatus;
import org.example.testcodesample.model.dto.UserCreateDto;
import org.example.testcodesample.model.dto.UserUpdateDto;
import org.example.testcodesample.repository.UserEntity;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlGroup;

@SpringBootTest
@TestPropertySource("classpath:test-application.properties")
@SqlGroup({
    @Sql(value = "/sql/user-service-test-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
    @Sql(value = "/sql/delete-all-data.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
})
class UserServiceTest {

    @Autowired
    private UserService userService;
    @MockBean
    private JavaMailSender mailSender;

    @Test
    void getByEmail은_Active_상태인_유저를_찾아올_수_있다() {
        //given
        String email = "test@test.com";

        //when
        UserEntity result = userService.getByEmail(email);

        //then
        assertThat(result.getNickname()).isEqualTo("test");
    }

    @Test
    void getByEmail은_Pending_상태인_유저를_찾아올_수_없다() {
        //given
        String email = "test2@test.com";

        //when
        //then
        assertThatThrownBy(()-> {
            UserEntity result = userService.getByEmail(email);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getById는_Active_상태인_유저를_찾아올_수_있다() {
        //given
        //when
        UserEntity result = userService.getById(1);

        //then
        assertThat(result.getNickname()).isEqualTo("test");
    }

    @Test
    void getById는_Pending_상태인_유저를_찾아올_수_없다() {
        //given
        String email = "test2@test.com";

        //when
        //then
        assertThatThrownBy(()-> {
            UserEntity result = userService.getById(2);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void UserCreateDto를_이용하여_유저를_생성할_수_있다() {
        //given
        UserCreateDto userCreateDto = UserCreateDto.builder()
            .email("test3@test.com")
            .address("Seoul")
            .nickname("kangnam")
            .build();

        BDDMockito.doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        //when
        UserEntity result = userService.create(userCreateDto);

        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
//        assertThat(result.getCertificationCode()).isEqualTo("T.T"); //FIXME
    }

    @Test
    void UserUpdateDto를_이용하여_유저를_생성할_수_있다() {
        //given
        UserUpdateDto updateDto = UserUpdateDto.builder()
            .address("Incheon")
            .nickname("sea")
            .build();

        //when
        userService.update(1, updateDto);

        //then
        UserEntity user = userService.getById(1);
        assertThat(user.getId()).isNotNull();
        assertThat(user.getAddress()).isEqualTo("Incheon");
        assertThat(user.getNickname()).isEqualTo("sea");
    }

    @Test
    void user를_로그인_시키면_마지막_로그인_시간이_변경된다() {
        //given
        //when
        userService.login(1);

        //then
        UserEntity userEntity = userService.getById(1);
        assertThat(userEntity.getLastLoginAt()).isGreaterThan(0);
        //        assertThat(result.getLastLoginAt()).isEqualTo("T.T"); //FIXME
    }

    @Test
    void PENDING_상태의_사용자는_인증_코드로_ACTIVE_시킬_수_있다() {
        //given
        //when
        userService.verifyEmail(2, "bbbbb-bbbb-bbbb");

        //then
        UserEntity userEntity = userService.getById(1);
        assertThat(userEntity.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void PENDING_상태의_사용자는_잘못된_인증_코드를_받으면_에러를_던진다() {
        //given
        //when
        //then
        assertThatThrownBy(()->{
            userService.verifyEmail(2, "bbbbb-bbbb-2222");
        }).isInstanceOf(CertificationCodeNotMatchedException.class);
    }

}
package org.example.testcodesample.user.infrastructure;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.example.testcodesample.user.domain.UserStatus;
import org.example.testcodesample.user.infrastructure.UserEntity;
import org.example.testcodesample.user.infrastructure.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@TestPropertySource("classpath:test-application.properties")
@Sql("/sql/user-repository-test-data.sql")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

   /* @Test
    void UserRepository_가_제대로_연결되었다() {
        //given
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("test@test.com");
        userEntity.setAddress("Seoul");
        userEntity.setNickname("test");
        userEntity.setStatus(UserStatus.ACTIVE);
        userEntity.setCertificationCode("aaaaaaaaaaaaa-aaaaaaaaaa-aaaaaaaaaaaaaaaaaa");

        //when
        UserEntity result = userRepository.save(userEntity);

        //then
        assertThat(result.getId()).isNotNull();
    }*/

    @Test
    void findByIdAndStatus_로_유저_데이터를_찾아올_수_있다() {
        //given
        //when
        Optional<UserEntity> byIdAndStatus = userRepository.findByIdAndStatus(1, UserStatus.ACTIVE);

        //then
        assertThat(byIdAndStatus.isPresent()).isTrue();

    }

    @Test
    void findByIdAndStatus_는_데이터가_없으면_Optional_empty_를_내려준다() {
        //given
        //when
        Optional<UserEntity> byIdAndStatus = userRepository.findByIdAndStatus(1, UserStatus.INACTIVE);

        //then
        assertThat(byIdAndStatus.isEmpty()).isTrue();

    }

    @Test
    void findByEmailAndStatus_로_유저_데이터를_찾아올_수_있다() {
        //given
        //when
        Optional<UserEntity> byIdAndStatus = userRepository.findByEmailAndStatus("test@test.com", UserStatus.ACTIVE);

        //then
        assertThat(byIdAndStatus.isPresent()).isTrue();

    }

    @Test
    void findByEmailAndStatus_는_데이터가_없으면_Optional_empty_를_내려준다() {
        //given
        //when
        Optional<UserEntity> byIdAndStatus = userRepository.findByEmailAndStatus("test@test.com", UserStatus.INACTIVE);

        //then
        assertThat(byIdAndStatus.isEmpty()).isTrue();

    }


}
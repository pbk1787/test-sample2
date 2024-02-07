package org.example.testcodesample.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.example.testcodesample.model.UserStatus;
import org.example.testcodesample.model.dto.UserUpdateDto;
import org.example.testcodesample.repository.UserEntity;
import org.example.testcodesample.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@SqlGroup({
    @Sql(value = "/sql/user-controller-test-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
    @Sql(value = "/sql/delete-all-data.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 사용자는_특정_유저의_개인정보는_소거된_정보를_응답_받을_수_있다() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(get("/api/users/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.email").value("test@test.com"))
            .andExpect(jsonPath("$.nickname").value("test"))
            .andExpect(jsonPath("$.address").doesNotExist()) //일반 조회로는 개인정보인 주소값을 받아오지 못함
            .andExpect(jsonPath("$.status").value("ACTIVE"));

    }

    @Test
    void 존재하지_않는_유저의_아이디로_api_호출할_경우_404_응답을_받는다() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(get("/api/users/123456789"))
            .andExpect(status().isNotFound())
            .andExpect(content().string("Users에서 ID 123456789를 찾을 수 없습니다."));
    }

    @Test
    void 사용자는_인증_코드로_계정을_활성화_시킬_수_있다() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(
            get("/api/users/2/verify")
                .queryParam("certificationCode","bbbbb-bbbb-bbbb"))
            .andExpect(status().isFound());

        UserEntity userEntity = userRepository.findById(2L).get();
        Assertions.assertThat(userEntity.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void 사용자는_내_정보를_불러올_때_개인정보인_주소도_갖고_올_수_있다() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(
                get("/api/users/me")
                    .header("EMAIL","test@test.com"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.email").value("test@test.com"))
            .andExpect(jsonPath("$.nickname").value("test"))
            .andExpect(jsonPath("$.address").value("Seoul"))
            .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void 사용자는_내_정보를_수정할_수_있다() throws Exception {
        //given
        UserUpdateDto userUpdateDto = UserUpdateDto.builder()
            .nickname("안녕")
            .address("Hello")
            .build();

        //when
        //then
        mockMvc.perform(
                put("/api/users/me")
                    .header("EMAIL","test@test.com")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(userUpdateDto))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.email").value("test@test.com"))
            .andExpect(jsonPath("$.nickname").value("안녕"))
            .andExpect(jsonPath("$.address").value("Hello"))
            .andExpect(jsonPath("$.status").value("ACTIVE"));
    }


}
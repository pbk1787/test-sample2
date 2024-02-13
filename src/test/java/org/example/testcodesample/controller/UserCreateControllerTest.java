package org.example.testcodesample.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.testcodesample.user.domain.UserCreate;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@SqlGroup({
    @Sql(value = "/sql/delete-all-data.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
})
class UserCreateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JavaMailSender mailSender;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 사용자는_회원가입을_할_수있고_회원가입된_사용자는_PENDING_상태이다() throws Exception {
        //given
        UserCreate userCreate = UserCreate.builder()
            .email("test@test.com")
            .nickname("test")
            .address("gangnam")
            .build();
        BDDMockito.doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        //when
        //then
        mockMvc.perform(
                post("/api/users")
                    .header("EMAIL", "test@test.com")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(userCreate)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.email").value("test@test.com"))
            .andExpect(jsonPath("$.nickname").value("test"))
//            .andExpect(jsonPath("$.address").doesNotExist()) //일반 조회로는 개인정보인 주소값을 받아오지 못함
            .andExpect(jsonPath("$.status").value("PENDING"));

    }

}
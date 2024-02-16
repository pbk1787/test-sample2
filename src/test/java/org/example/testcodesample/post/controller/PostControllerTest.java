package org.example.testcodesample.post.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.testcodesample.post.domain.PostUpdate;
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
    @Sql(value = "/sql/post-controller-test-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_CLASS),
    @Sql(value = "/sql/delete-all-data.sql", executionPhase = ExecutionPhase.AFTER_TEST_CLASS)
})
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 사용자는_게시물을_단건_조회_할_수_있다() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(get("/api/posts/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.content").value("helloworld"))
            .andExpect(jsonPath("$.writer.id").isNumber())
            .andExpect(jsonPath("$.writer.email").value("test@test.com"))
            .andExpect(jsonPath("$.writer.nickname").value("test"));
    }

    @Test
    void 존재하지_않는_게시물을_조회_할_경우_에러가_난다() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(get("/api/posts/123456"))
            .andExpect(status().isNotFound())
            .andExpect(content().string("Posts에서 ID 123456를 찾을 수 없습니다."));
    }


    @Test
    void 사용자는_게시물을_수정할_수_있다() throws Exception {
        //given
        PostUpdate postUpdate = PostUpdate.builder()
            .content("안녕_테스트")
            .build();

        //when
        //then
        mockMvc.perform(
                put("/api/posts/1")
                    .header("EMAIL", "test@test.com")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(postUpdate))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.content").value("안녕_테스트"))
            .andExpect(jsonPath("$.writer.id").isNumber())
            .andExpect(jsonPath("$.writer.email").value("test@test.com"))
            .andExpect(jsonPath("$.writer.nickname").value("test"));
    }

}
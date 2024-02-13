package org.example.testcodesample.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.example.testcodesample.post.domain.PostCreate;
import org.example.testcodesample.post.domain.PostUpdate;
import org.example.testcodesample.post.infrastructure.PostEntity;
import org.example.testcodesample.post.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Test
    void getPostById는_존재하는_게시물을_내려준다() {
        //given
        //when
        PostEntity result = postService.getPostById(1);

        //then
        assertThat(result.getContent()).isEqualTo("helloworld");
        assertThat(result.getWriter().getEmail()).isEqualTo("test@test.com");
    }

    @Test
    void userCreateDto_를_이용하여_게시물을_생성할_수_있다() {
        //given
        PostCreate postCreate = PostCreate.builder()
            .writerId(1)
            .content("test")
            .build();

        //when
        PostEntity result = postService.create(postCreate);

        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getContent()).isEqualTo("test");
        assertThat(result.getCreatedAt()).isGreaterThan(0);

    }

    @Test
    void PostUpdateDto_를_이용하여_게시물을_수정할_수_있다() {
        //given
        PostUpdate postUpdate = PostUpdate.builder()
            .content("tteesstt")
            .build();

        //when
        PostEntity result = postService.update(1, postUpdate);

        //then
        assertThat(result.getContent()).isEqualTo("tteesstt");
        assertThat(result.getModifiedAt()).isGreaterThan(0);
    }
}
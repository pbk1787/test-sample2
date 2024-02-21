package org.example.testcodesample.post.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.example.testcodesample.mock.TestContainer;
import org.example.testcodesample.post.controller.response.PostResponse;
import org.example.testcodesample.post.domain.PostCreate;
import org.example.testcodesample.user.domain.User;
import org.example.testcodesample.user.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

class PostCreateControllerTest {

    @Test
    void 사용자는_게시물을_작성할_수_있다() {
        //given
        TestContainer testContainer = TestContainer.builder()
            .clockHolder(() -> 1679530673958L)
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

        PostCreate postCreate = PostCreate.builder()
            .writerId(1)
            .content("hello~")
            .build();

        //when
        ResponseEntity<PostResponse> result = testContainer.postCreateController
            .create(postCreate);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getContent()).isEqualTo("hello~");
        assertThat(result.getBody().getCreatedAt()).isEqualTo(1679530673958L);
        assertThat(result.getBody().getWriter().getId()).isEqualTo(1L);
        assertThat(result.getBody().getWriter().getNickname()).isEqualTo("test");
        assertThat(result.getBody().getWriter().getEmail()).isEqualTo("test@test.com");
    }

}
package org.example.testcodesample.post.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.example.testcodesample.common.domain.exception.ResourceNotFoundException;
import org.example.testcodesample.mock.TestContainer;
import org.example.testcodesample.post.controller.response.PostResponse;
import org.example.testcodesample.post.domain.Post;
import org.example.testcodesample.post.domain.PostUpdate;
import org.example.testcodesample.user.domain.User;
import org.example.testcodesample.user.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

class PostControllerTest {

    @Test
    void 사용자는_게시물을_단건_조회_할_수_있다() {
        //given
        TestContainer testContainer = TestContainer.builder()
            .build();

        User writer = User.builder()
            .id(1L)
            .email("test@test.com")
            .nickname("test")
            .address("Seoul")
            .certificationCode("aaaaa-aaaa-aaaaa")
            .status(UserStatus.ACTIVE)
            .lastLoginAt(0L)
            .build();

        testContainer.userRepository.save(writer);

        testContainer.postRepository.save(Post.builder()
            .id(1L)
            .writer(writer)
            .content("hello~")
            .createdAt(1679530673958L)
            .build());

        //when
        ResponseEntity<PostResponse> result = testContainer.postController
            .getPostById(1);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getContent()).isEqualTo("hello~");
        assertThat(result.getBody().getCreatedAt()).isEqualTo(1679530673958L);
        assertThat(result.getBody().getWriter().getId()).isEqualTo(1L);
        assertThat(result.getBody().getWriter().getNickname()).isEqualTo("test");
        assertThat(result.getBody().getWriter().getEmail()).isEqualTo("test@test.com");
    }

    @Test
    void 존재하지_않는_게시물을_조회_할_경우_에러가_난다() throws Exception {
        //given
        TestContainer testContainer = TestContainer.builder()
            .build();

        User writer = User.builder()
            .id(1L)
            .email("test@test.com")
            .nickname("test")
            .address("Seoul")
            .certificationCode("aaaaa-aaaa-aaaaa")
            .status(UserStatus.ACTIVE)
            .lastLoginAt(0L)
            .build();

        testContainer.userRepository.save(writer);

        /*testContainer.postRepository.save(Post.builder()
            .writer(writer)
            .content("hello~")
            .createdAt(1679530673958L)
            .build());*/

        //when
        //then
        assertThatThrownBy(() -> testContainer.postController
            .getPostById(1234)).isInstanceOf(ResourceNotFoundException.class);
    }


    @Test
    void 사용자는_게시물을_수정할_수_있다() throws Exception {
        //given
        TestContainer testContainer = TestContainer.builder()
            .clockHolder(() -> 1679530673958L)
            .build();

        User writer = User.builder()
            .id(1L)
            .email("test@test.com")
            .nickname("test")
            .address("Seoul")
            .certificationCode("aaaaa-aaaa-aaaaa")
            .status(UserStatus.ACTIVE)
            .lastLoginAt(0L)
            .build();

        testContainer.userRepository.save(writer);

        testContainer.postRepository.save(Post.builder()
            .id(1L)
            .writer(writer)
            .content("hello~")
            .createdAt(0L)
            .build());

        //when
        ResponseEntity<PostResponse> result = testContainer.postController
            .updatePost(1, PostUpdate.builder()
                .content("안녕_테스트~")
                .build());

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getContent()).isEqualTo("안녕_테스트~");
        assertThat(result.getBody().getCreatedAt()).isEqualTo(0L);
        assertThat(result.getBody().getModifiedAt()).isEqualTo(1679530673958L);
        assertThat(result.getBody().getWriter().getId()).isEqualTo(1L);
        assertThat(result.getBody().getWriter().getNickname()).isEqualTo("test");
        assertThat(result.getBody().getWriter().getEmail()).isEqualTo("test@test.com");
    }

}
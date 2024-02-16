package org.example.testcodesample.post.controller.response;

import static org.assertj.core.api.Assertions.assertThat;

import org.example.testcodesample.post.domain.Post;
import org.example.testcodesample.user.domain.User;
import org.example.testcodesample.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

class PostResponseTest {

    @Test
    void Post로_응답을_생성할_수_있다() {
        //given
        Post post = Post.builder()
            .content("hello")
            .writer(User.builder()
                .id(1L)
                .email("test@test.com")
                .nickname("test")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaa-aaa")
                .build())
            .build();

        //when
        PostResponse postResponse = PostResponse.from(post);

        //then
        //then
        assertThat(postResponse.getContent()).isEqualTo("hello");
        assertThat(postResponse.getWriter().getEmail()).isEqualTo("test@test.com");
        assertThat(postResponse.getWriter().getNickname()).isEqualTo("test");
        assertThat(postResponse.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

}
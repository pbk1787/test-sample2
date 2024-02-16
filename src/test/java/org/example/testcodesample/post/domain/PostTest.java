package org.example.testcodesample.post.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.example.testcodesample.user.domain.User;
import org.example.testcodesample.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

class PostTest {

    @Test
    void PostCreate로_게시물을_만들수_있다() {
        //given
        PostCreate postCreate = PostCreate.builder()
            .writerId(1)
            .content("hello")
            .build();

        User writer = User.builder()
            .id(1L)
            .email("test@test.com")
            .nickname("test")
            .address("Seoul")
            .status(UserStatus.ACTIVE)
            .certificationCode("aaa-aaa")
            .build();

        //when
        Post post = Post.from(writer, postCreate);

        //then
        assertThat(post.getContent()).isEqualTo("hello");
        assertThat(post.getWriter().getEmail()).isEqualTo("test@test.com");
        assertThat(post.getWriter().getNickname()).isEqualTo("test");
        assertThat(post.getWriter().getAddress()).isEqualTo("Seoul");
        assertThat(post.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(post.getWriter().getCertificationCode()).isEqualTo("aaa-aaa");
    }

    @Test
    void PostCreate로_게시물을_수정할_수_있다() {
        //given
        //when
        //then
    }

}
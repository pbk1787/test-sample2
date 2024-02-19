package org.example.testcodesample.post.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.example.testcodesample.mock.TestClockHolder;
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
        Post post = Post.from(writer, postCreate, new TestClockHolder(16793067398L));

        //then
        assertThat(post.getContent()).isEqualTo("hello");
        assertThat(post.getCreatedAt()).isEqualTo(16793067398L);
        assertThat(post.getWriter().getEmail()).isEqualTo("test@test.com");
        assertThat(post.getWriter().getNickname()).isEqualTo("test");
        assertThat(post.getWriter().getAddress()).isEqualTo("Seoul");
        assertThat(post.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(post.getWriter().getCertificationCode()).isEqualTo("aaa-aaa");
    }

    @Test
    void PostCreate로_게시물을_수정할_수_있다() {
        //given
        PostUpdate postUpdate = PostUpdate.builder()
            .content("test!")
            .build();

        User writer = User.builder()
            .id(1L)
            .email("test@test.com")
            .nickname("test")
            .address("Seoul")
            .status(UserStatus.ACTIVE)
            .certificationCode("aaa-aaa")
            .build();

        Post post = Post.builder()
            .id(1L)
            .content("hello")
            .createdAt(1678530673958L)
            .modifiedAt(0L)
            .writer(writer)
            .build();

        //when
        post = post.update(postUpdate, new TestClockHolder(1679530673958L));

        //then
        assertThat(post.getContent()).isEqualTo("test!");
        assertThat(post.getCreatedAt()).isEqualTo(1678530673958L);
        assertThat(post.getModifiedAt()).isEqualTo(1679530673958L);
//        assertThat(post.getWriter().getEmail()).isEqualTo("test@test.com");
//        assertThat(post.getWriter().getNickname()).isEqualTo("test");
//        assertThat(post.getWriter().getAddress()).isEqualTo("Seoul");
//        assertThat(post.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
//        assertThat(post.getWriter().getCertificationCode()).isEqualTo("aaa-aaa");
    }

}
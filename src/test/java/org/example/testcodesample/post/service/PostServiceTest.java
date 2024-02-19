package org.example.testcodesample.post.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.example.testcodesample.mock.FakePostRepository;
import org.example.testcodesample.mock.FakeUserRepository;
import org.example.testcodesample.mock.TestClockHolder;
import org.example.testcodesample.post.domain.Post;
import org.example.testcodesample.post.domain.PostCreate;
import org.example.testcodesample.post.domain.PostUpdate;
import org.example.testcodesample.user.domain.User;
import org.example.testcodesample.user.domain.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PostServiceTest {

    private PostService postService;

    @BeforeEach
    void init() {
        FakePostRepository fakePostRepository = new FakePostRepository();
        FakeUserRepository fakeUserRepository = new FakeUserRepository();

        this.postService = new PostService(
            fakePostRepository,
            fakeUserRepository,
            new TestClockHolder(16793067398L)
        );

        User user1 = User.builder()
            .id(1L)
            .email("test@test.com")
            .nickname("test")
            .address("Seoul")
            .certificationCode("aaaaa-aaaa-aaaaa")
            .status(UserStatus.ACTIVE)
            .lastLoginAt(0L)
            .build();

        fakeUserRepository.save(user1);

        fakeUserRepository.save(User.builder()
            .id(2L)
            .email("test2@test.com")
            .nickname("test2")
            .address("Seoul")
            .certificationCode("bbbbb-bbbb-bbbb")
            .status(UserStatus.PENDING)
            .lastLoginAt(0L)
            .build());

        fakePostRepository.save(Post.builder()
            .id(1L)
            .content("helloworld")
            .createdAt(16783067398L)
            .modifiedAt(0L)
            .writer(user1)
            .build());
    }

    @Test
    void getPostById는_존재하는_게시물을_내려준다() {
        //given
        //when
        Post result = postService.getPostById(1);

        //then
        assertThat(result.getContent()).isEqualTo("helloworld");
        assertThat(result.getWriter().getEmail()).isEqualTo("test@test.com");
    }

    @Test
    void PostCreateDto_를_이용하여_게시물을_생성할_수_있다() {
        //given
        PostCreate postCreate = PostCreate.builder()
            .writerId(1)
            .content("test")
            .build();

        //when
        Post result = postService.create(postCreate);

        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getContent()).isEqualTo("test");
        assertThat(result.getCreatedAt()).isEqualTo(16793067398L);

    }

    @Test
    void PostUpdateDto_를_이용하여_게시물을_수정할_수_있다() {
        //given
        PostUpdate postUpdate = PostUpdate.builder()
            .content("tteesstt")
            .build();

        //when
        Post result = postService.update(1, postUpdate);

        //then
        assertThat(result.getContent()).isEqualTo("tteesstt");
        assertThat(result.getModifiedAt()).isEqualTo(16793067398L);
    }
}
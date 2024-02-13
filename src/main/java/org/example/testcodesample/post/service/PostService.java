package org.example.testcodesample.post.service;

import java.time.Clock;
import lombok.RequiredArgsConstructor;
import org.example.testcodesample.common.domain.exception.ResourceNotFoundException;
import org.example.testcodesample.post.domain.PostCreate;
import org.example.testcodesample.post.domain.PostUpdate;
import org.example.testcodesample.post.infrastructure.PostEntity;
import org.example.testcodesample.post.infrastructure.PostRepository;
import org.example.testcodesample.user.infrastructure.UserEntity;
import org.example.testcodesample.user.service.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    public PostEntity getPostById(long id) {
        return postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Posts", id));
    }

    public PostEntity create(PostCreate postCreate) {
        UserEntity userEntity = userService.getById(postCreate.getWriterId());
        PostEntity postEntity = new PostEntity();
        postEntity.setWriter(userEntity);
        postEntity.setContent(postCreate.getContent());
        postEntity.setCreatedAt(Clock.systemUTC().millis());
        return postRepository.save(postEntity);
    }

    public PostEntity update(long id, PostUpdate postUpdate) {
        PostEntity postEntity = getPostById(id);
        postEntity.setContent(postUpdate.getContent());
        postEntity.setModifiedAt(Clock.systemUTC().millis());
        return postRepository.save(postEntity);
    }
}
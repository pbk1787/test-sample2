package org.example.testcodesample.service;

import java.time.Clock;
import lombok.RequiredArgsConstructor;
import org.example.testcodesample.exception.ResourceNotFoundException;
import org.example.testcodesample.model.dto.PostCreateDto;
import org.example.testcodesample.model.dto.PostUpdateDto;
import org.example.testcodesample.repository.PostEntity;
import org.example.testcodesample.repository.PostRepository;
import org.example.testcodesample.repository.UserEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    public PostEntity getPostById(long id) {
        return postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Posts", id));
    }

    public PostEntity createPost(PostCreateDto postCreateDto) {
        UserEntity userEntity = userService.getByIdOrElseThrow(postCreateDto.getWriterId());
        PostEntity postEntity = new PostEntity();
        postEntity.setWriter(userEntity);
        postEntity.setContent(postCreateDto.getContent());
        postEntity.setCreatedAt(Clock.systemUTC().millis());
        return postRepository.save(postEntity);
    }

    public PostEntity updatePost(long id, PostUpdateDto postUpdateDto) {
        PostEntity postEntity = getPostById(id);
        postEntity.setContent(postUpdateDto.getContent());
        postEntity.setModifiedAt(Clock.systemUTC().millis());
        return postRepository.save(postEntity);
    }
}
package org.example.testcodesample.post.service;

import lombok.RequiredArgsConstructor;
import org.example.testcodesample.common.domain.exception.ResourceNotFoundException;
import org.example.testcodesample.common.service.port.ClockHolder;
import org.example.testcodesample.post.controller.port.PostService;
import org.example.testcodesample.post.domain.Post;
import org.example.testcodesample.post.domain.PostCreate;
import org.example.testcodesample.post.domain.PostUpdate;
import org.example.testcodesample.post.service.port.PostRepository;
import org.example.testcodesample.user.domain.User;
import org.example.testcodesample.user.service.port.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ClockHolder clockHolder;

    @Override
    public Post getPostById(long id) {
        return postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Posts", id));
    }

    @Override
    public Post create(PostCreate postCreate) {
        User writer = userRepository.getById(postCreate.getWriterId());
        Post post = Post.from(writer, postCreate, clockHolder);
        return postRepository.save(post);
    }

    @Override
    public Post update(long id, PostUpdate postUpdate) {
        Post post = getPostById(id);
        post = post.update(postUpdate, clockHolder);
        return postRepository.save(post);
    }
}
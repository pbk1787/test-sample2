package org.example.testcodesample.post.service;

import lombok.RequiredArgsConstructor;
import org.example.testcodesample.common.domain.exception.ResourceNotFoundException;
import org.example.testcodesample.post.domain.Post;
import org.example.testcodesample.post.domain.PostCreate;
import org.example.testcodesample.post.domain.PostUpdate;
import org.example.testcodesample.post.service.port.PostRepository;
import org.example.testcodesample.user.domain.User;
import org.example.testcodesample.user.service.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    public Post getPostById(long id) {
        return postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Posts", id));
    }

    public Post create(PostCreate postCreate) {
        User writer = userService.getById(postCreate.getWriterId());
        Post post = Post.from(writer, postCreate);
        return postRepository.save(post);
    }

    public Post update(long id, PostUpdate postUpdate) {
        Post post = getPostById(id);
        post = post.update(postUpdate);
        return postRepository.save(post);
    }
}
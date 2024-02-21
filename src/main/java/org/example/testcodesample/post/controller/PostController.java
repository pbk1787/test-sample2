package org.example.testcodesample.post.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.testcodesample.post.controller.port.PostService;
import org.example.testcodesample.post.controller.response.PostResponse;
import org.example.testcodesample.post.domain.PostUpdate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "게시물(posts)")
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getById(@PathVariable long id) {
        return ResponseEntity
            .ok()
            .body(PostResponse.from(postService.getPostById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> update(@PathVariable long id, @RequestBody PostUpdate postUpdate) {
        return ResponseEntity
            .ok()
            .body(PostResponse.from(postService.update(id, postUpdate)));
    }
}
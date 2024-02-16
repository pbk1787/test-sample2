package org.example.testcodesample.post.controller.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.testcodesample.post.domain.Post;
import org.example.testcodesample.user.controller.response.UserResponse;

@Getter
@Setter
public class PostResponse {

    private Long id;
    private String content;
    private Long createdAt;
    private Long modifiedAt;
    private UserResponse writer;

    @Builder
    public PostResponse(Long id, String content, Long createdAt, Long modifiedAt, UserResponse writer) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.writer = writer;
    }

    public static PostResponse from(Post post) {
        return PostResponse.builder()
            .id(post.getId())
            .content(post.getContent())
            .createdAt(post.getCreatedAt())
            .modifiedAt(post.getModifiedAt())
            .writer(UserResponse.from(post.getWriter()))
            .build();
    }
}

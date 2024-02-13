package org.example.testcodesample.post.controller.response;

import lombok.Getter;
import lombok.Setter;
import org.example.testcodesample.user.controller.response.UserResponse;

@Getter
@Setter
public class PostResponse {

    private Long id;
    private String content;
    private Long createdAt;
    private Long modifiedAt;
    private UserResponse writer;
}

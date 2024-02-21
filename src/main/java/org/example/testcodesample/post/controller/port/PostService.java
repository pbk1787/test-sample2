package org.example.testcodesample.post.controller.port;

import org.example.testcodesample.post.domain.Post;
import org.example.testcodesample.post.domain.PostCreate;
import org.example.testcodesample.post.domain.PostUpdate;

public interface PostService {

    Post getPostById(long id);

    Post create(PostCreate postCreate);

    Post update(long id, PostUpdate postUpdate);

}

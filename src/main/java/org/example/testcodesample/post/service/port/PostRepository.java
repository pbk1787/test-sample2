package org.example.testcodesample.post.service.port;

import java.util.Optional;
import org.example.testcodesample.post.domain.Post;

public interface PostRepository {

    Optional<Post> findById(long id);

    Post save(Post postEntity);
}

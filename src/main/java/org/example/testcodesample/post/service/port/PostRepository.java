package org.example.testcodesample.post.service.port;

import java.util.Optional;
import org.example.testcodesample.post.infrastructure.PostEntity;

public interface PostRepository {

    Optional<PostEntity> findById(long id);

    PostEntity save(PostEntity postEntity);
}

package org.example.testcodesample.post.infrastructure;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.testcodesample.post.domain.Post;
import org.example.testcodesample.post.service.port.PostRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository {

    private final PostJpaRepository postJpaRepository;

    @Override
    public Optional<Post> findById(long id) {
        return postJpaRepository.findById(id).map(PostEntity::toModel);
    }

    @Override
    public Post save(Post post) {
        return postJpaRepository.save(PostEntity.fromModel(post)).toModel();
    }
}

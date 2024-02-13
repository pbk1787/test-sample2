package org.example.testcodesample.post.infrastructure;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.testcodesample.post.service.port.PostRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository {

    private final PostJpaRepository postJpaRepository;

    @Override
    public Optional<PostEntity> findById(long id) {
        return postJpaRepository.findById(id);
    }

    @Override
    public PostEntity save(PostEntity postEntity) {
        return postJpaRepository.save(postEntity);
    }
}

package org.example.testcodesample.user.infrastructure;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.testcodesample.user.domain.User;
import org.example.testcodesample.user.domain.UserStatus;
import org.example.testcodesample.user.service.port.UserRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public User save(User user) {
        //유저 도메인에서 영속성 도메인으로 변환하는 것보다 영속성 도메인에서 유저 도메인을 받아 변환하는 것이 더 좋음
        // 도메인은 영속성(인프라) 레이어의 정보를 모르는 것이 좋다
        return userJpaRepository.save(UserEntity.fromModel(user)).toModel();
    }
    
    @Override
    public Optional<User> findById(long id) {
        return userJpaRepository.findById(id).map(UserEntity::toModel);
    }

    @Override
    public Optional<User> findByIdAndStatus(long id, UserStatus userStatus) {
        return userJpaRepository.findByIdAndStatus(id, userStatus).map(UserEntity::toModel);
    }

    @Override
    public Optional<User> findByEmailAndStatus(String email, UserStatus userStatus) {
        return userJpaRepository.findByEmailAndStatus(email, userStatus).map(UserEntity::toModel);
    }

}

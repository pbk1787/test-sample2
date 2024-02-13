package org.example.testcodesample.user.service.port;

import java.util.Optional;
import org.example.testcodesample.user.domain.UserStatus;
import org.example.testcodesample.user.infrastructure.UserEntity;

public interface UserRepository {

    Optional<UserEntity> findById(long id);

    Optional<UserEntity> findByIdAndStatus(long id, UserStatus userStatus);

    Optional<UserEntity> findByEmailAndStatus(String email, UserStatus userStatus);

    UserEntity save(UserEntity userEntity);

}

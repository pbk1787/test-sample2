package org.example.testcodesample.user.service.port;

import java.util.Optional;
import org.example.testcodesample.user.domain.User;
import org.example.testcodesample.user.domain.UserStatus;

public interface UserRepository {

    Optional<User> findById(long id);

    Optional<User> findByIdAndStatus(long id, UserStatus userStatus);

    Optional<User> findByEmailAndStatus(String email, UserStatus userStatus);

    User save(User user);

}

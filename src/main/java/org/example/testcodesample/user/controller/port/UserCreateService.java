package org.example.testcodesample.user.controller.port;

import org.example.testcodesample.user.domain.User;
import org.example.testcodesample.user.domain.UserCreate;

public interface UserCreateService {

    User create(UserCreate userCreate);
}

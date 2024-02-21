package org.example.testcodesample.user.controller.port;

import org.example.testcodesample.user.domain.User;
import org.example.testcodesample.user.domain.UserCreate;
import org.example.testcodesample.user.domain.UserUpdate;

public interface UserService {

    User getByEmail(String email);

    User getById(long id);

    User create(UserCreate userCreate);

    User update(long id, UserUpdate userUpdate);

    void login(long id);

    void verifyEmail(long id, String certificationCode);

}

package org.example.testcodesample.user.controller.port;

import org.example.testcodesample.user.domain.User;
import org.example.testcodesample.user.domain.UserUpdate;

public interface UserUpdateService {

    User update(long id, UserUpdate userUpdate);

}

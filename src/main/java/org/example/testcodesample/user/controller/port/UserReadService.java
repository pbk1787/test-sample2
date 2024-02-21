package org.example.testcodesample.user.controller.port;

import org.example.testcodesample.user.domain.User;

public interface UserReadService {

    User getByEmail(String email);

    User getById(long id);

}

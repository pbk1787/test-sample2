package org.example.testcodesample.user.controller.port;

public interface AuthenticationService {

    void login(long id);

    void verifyEmail(long id, String certificationCode);

}

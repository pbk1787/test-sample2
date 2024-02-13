package org.example.testcodesample.user.service.port;

public interface MailSender {

    public void send(String email, String title, String context);

}

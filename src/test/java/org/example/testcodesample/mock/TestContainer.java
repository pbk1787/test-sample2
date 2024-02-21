package org.example.testcodesample.mock;

import lombok.Builder;
import org.example.testcodesample.common.service.port.ClockHolder;
import org.example.testcodesample.common.service.port.UuidHolder;
import org.example.testcodesample.post.controller.PostController;
import org.example.testcodesample.post.controller.PostCreateController;
import org.example.testcodesample.post.controller.port.PostService;
import org.example.testcodesample.post.service.PostServiceImpl;
import org.example.testcodesample.post.service.port.PostRepository;
import org.example.testcodesample.user.controller.UserController;
import org.example.testcodesample.user.controller.UserCreateController;
import org.example.testcodesample.user.controller.port.AuthenticationService;
import org.example.testcodesample.user.controller.port.UserCreateService;
import org.example.testcodesample.user.controller.port.UserReadService;
import org.example.testcodesample.user.controller.port.UserUpdateService;
import org.example.testcodesample.user.service.CertificationService;
import org.example.testcodesample.user.service.UserServiceImpl;
import org.example.testcodesample.user.service.port.MailSender;
import org.example.testcodesample.user.service.port.UserRepository;

public class TestContainer {

    public final MailSender mailSender;
    public final UserRepository userRepository;
    public final PostRepository postRepository;
    public final UserReadService userReadService;
    public final UserCreateService userCreateService;
    public final UserUpdateService userUpdateService;
    public final AuthenticationService authenticationService;
    public final PostService postService;
    public final CertificationService certificationService;
    public final UserController userController;
    public final UserCreateController userCreateController;
    public final PostController postController;
    public final PostCreateController postCreateController;

    @Builder
    public TestContainer(ClockHolder clockHolder, UuidHolder uuidHolder) {
        this.mailSender = new FakeMailSender();
        this.userRepository = new FakeUserRepository();
        this.postRepository = new FakePostRepository();
        this.postService = new PostServiceImpl(
            this.postRepository,
            this.userRepository,
            clockHolder
        );
        this.certificationService = new CertificationService(this.mailSender);
        UserServiceImpl userService = new UserServiceImpl(
            this.userRepository,
            new CertificationService(this.mailSender),
            uuidHolder,
            clockHolder);

        this.userReadService = userService;
        this.userCreateService = userService;
        this.userUpdateService = userService;
        this.authenticationService = userService;
        this.userController = UserController.builder()
            .userReadService(userReadService)
            .userCreateService(userCreateService)
            .userUpdateService(userUpdateService)
            .authenticationService(authenticationService)
            .build();
        this.userCreateController = new UserCreateController(userCreateService);
        this.postController = new PostController(postService);
        this.postCreateController = new PostCreateController(postService);


    }

}

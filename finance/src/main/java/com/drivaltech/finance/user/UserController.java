package com.drivaltech.finance.user;

import com.drivaltech.finance.dto.CreateUserRequest;
import com.drivaltech.finance.dto.UserResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserResponse createUser(@RequestBody CreateUserRequest request) {

        User user = userService.createUser(
                request.getUsername(),
                request.getPassword(),
                request.getRole()
        );

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getRole(),
                user.isActive()
        );
    }
    @GetMapping
    public List<UserResponse> getAllUsers() {

        List<User> users = userService.findAllUsers();

        return users.stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getRole(),
                        user.isActive()
                ))
                .toList();
    }
}

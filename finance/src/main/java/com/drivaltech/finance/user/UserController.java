package com.drivaltech.finance.user;

import com.drivaltech.finance.dto.CreateUserRequest;
import com.drivaltech.finance.dto.PaginationResponse;
import com.drivaltech.finance.dto.UpdateUserRequest;
import com.drivaltech.finance.dto.UserResponse;
import com.drivaltech.finance.mapper.UserMapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping
    public UserResponse createUser(@RequestBody CreateUserRequest request) {

        User user = userService.createUser(
                request.getUsername(),
                request.getPassword()
        );

        return userMapper.toResponse(user);
    }

    @GetMapping
    public PaginationResponse<UserResponse> getAllUsers(Pageable pageable) {

        Page<User> usersPage = userService.findAllUsers(pageable);

        Page<UserResponse> responsePage =
                usersPage.map(userMapper::toResponse);

        return new PaginationResponse<>(responsePage);
    }

    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable UUID id) {

        User user = userService.findUserById(id);

        return userMapper.toResponse(user);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateUser(@PathVariable UUID id) {

        userService.deactivateUser(id);

        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}")
    public UserResponse updateUser(
            @PathVariable UUID id,
            @RequestBody UpdateUserRequest request
    ) {

        User updatedUser = userService.updateUser(id, request);

        return userMapper.toResponse(updatedUser);
    }
}
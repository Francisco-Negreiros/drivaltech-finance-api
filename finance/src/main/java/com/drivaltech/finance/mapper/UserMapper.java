package com.drivaltech.finance.mapper;

import com.drivaltech.finance.user.User;
import com.drivaltech.finance.dto.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getRoles()
                        .stream()
                        .map(Enum::name)
                        .toList(),
                user.isActive()
        );
    }
}

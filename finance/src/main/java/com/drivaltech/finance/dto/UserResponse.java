package com.drivaltech.finance.dto;

import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class UserResponse {

    private UUID id;
    private String username;
    private List<String> roles;
    private boolean active;

    public UserResponse(UUID id, String username, List<String> roles, boolean active) {
        this.id = id;
        this.username = username;
        this.roles = roles;
        this.active = active;
    }

}

package com.drivaltech.finance.dto;

import com.drivaltech.finance.user.Role;
import java.util.UUID;

public class UserResponse {

    private UUID id;
    private String username;
    private Role role;
    private boolean active;

    public UserResponse(UUID id, String username, Role role, boolean active) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.active = active;
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Role getRole() {
        return role;
    }

    public boolean isActive() {
        return active;
    }
}

package com.drivaltech.finance.dto;

import com.drivaltech.finance.user.Role;
import java.util.Set;

public class UpdateUserRequest {

    private String username;
    private Set<Role> roles;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
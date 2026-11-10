package net.pastry.auth.account.model;

import net.pastry.auth.user.Role;
import net.pastry.auth.user.User;

import java.util.UUID;

public record UserResponse(UUID id, String name, Role role) {
    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getRole());
    }
}

package net.pastry.auth.security.user;

import lombok.ToString;
import lombok.Value;
import net.pastry.auth.user.Role;
import net.pastry.auth.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Value
public class AuthUser implements UserDetails {
    UUID id;
    String username;
    @ToString.Exclude
    String password;
    Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    public static AuthUser from(User user) {
        return new AuthUser(user.getId(), user.getName(), user.getPasswordHash(), user.getRole());
    }
}

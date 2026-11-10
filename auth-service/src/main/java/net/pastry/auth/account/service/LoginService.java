package net.pastry.auth.account.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.pastry.auth.security.user.AuthUser;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final AuthenticationManager authenticationManager;

    public AuthUser login(@NonNull String username, @NonNull String password) {
        var token = UsernamePasswordAuthenticationToken.unauthenticated(username, password);
        var authentication = authenticationManager.authenticate(token);

        return (AuthUser) authentication.getPrincipal();
    }
}

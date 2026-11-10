package net.pastry.auth.account.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.pastry.auth.account.model.LoginRequest;
import net.pastry.auth.account.service.LoginService;
import net.pastry.auth.account.service.RegistrationService;
import net.pastry.auth.account.model.RegisterRequest;
import net.pastry.auth.account.model.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthController {
    RegistrationService registrationService;
    LoginService loginService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    UserResponse register(@Valid @RequestBody RegisterRequest request) {
        var user = registrationService.register(request.username(), request.password());
        return UserResponse.from(user);
    }

    @PostMapping("/login")
    UserResponse login(@Valid @RequestBody LoginRequest request) {
        var user = loginService.login(request.username(), request.password());
        return UserResponse.from(user);
    }
}

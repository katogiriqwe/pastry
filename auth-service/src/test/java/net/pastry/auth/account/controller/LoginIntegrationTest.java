package net.pastry.auth.account.controller;

import net.pastry.auth.AbstractIntegrationTest;
import net.pastry.auth.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LoginIntegrationTest extends AbstractIntegrationTest {
    private static final String LOGIN_URL = "/auth/login";

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    void login_withValidCredentials_returns200AndUser() throws Exception {
        userRepository.save(User.create("vasya", passwordEncoder.encode("qwerty123")));

        postJson(LOGIN_URL, """
                {"username": "vasya", "password": "qwerty123"}
                """)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("vasya"))
                .andExpect(jsonPath("$.role").value("USER"))
                .andExpect(jsonPath("$.passwordHash").doesNotExist());
    }
}
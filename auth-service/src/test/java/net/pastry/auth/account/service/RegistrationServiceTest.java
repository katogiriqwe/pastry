package net.pastry.auth.account.service;

import net.pastry.auth.account.exception.UsernameAlreadyTakenException;
import net.pastry.auth.user.User;
import net.pastry.auth.user.UserRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {
    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    RegistrationService registrationService;

    @Captor
    ArgumentCaptor<User> userCaptor;

    @Test
    void register_savesUserWithHashedPassword() {
        when(passwordEncoder.encode("qwerty123")).thenReturn("hashed");

        registrationService.register("user", "qwerty123");

        verify(userRepository).save(userCaptor.capture());

        var user = userCaptor.getValue();
        assertThat(user.getPasswordHash()).isEqualTo("hashed");
    }

    @Test
    void register_whenUsernameIndexViolated_throwsUsernameAlreadyTaken() {
        var exception = constraintViolation("ux_users_name_lower");

        when(passwordEncoder.encode("qwerty123")).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenThrow(exception);

        assertThatThrownBy(() -> registrationService.register("user", "qwerty123"))
                .isInstanceOf(UsernameAlreadyTakenException.class);
    }

    @Test
    void register_whenOtherConstraint_throwsOriginalException() {
        var exception = constraintViolation("some_constraint");

        when(passwordEncoder.encode("qwerty123")).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenThrow(exception);

        assertThatThrownBy(() -> registrationService.register("user", "qwerty123"))
                .isSameAs(exception);
    }

    private static DataIntegrityViolationException constraintViolation(String constraintName) {
        var cause = new ConstraintViolationException("constraint violated", new SQLException(), constraintName);
        return new DataIntegrityViolationException("constraint violated", cause);
    }
}

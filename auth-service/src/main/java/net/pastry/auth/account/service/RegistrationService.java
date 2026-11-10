package net.pastry.auth.account.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.pastry.auth.account.exception.UsernameAlreadyTakenException;
import net.pastry.auth.user.User;
import net.pastry.auth.user.UserRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class RegistrationService {
    private static final String USERNAME_UNIQUE_INDEX = "ux_users_name_lower";

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    public User register(String username, String rawPassword) {
        var password = passwordEncoder.encode(rawPassword);
        var user = User.create(username, password);

        try {
            userRepository.save(user);

            return user;
        } catch (DataIntegrityViolationException exception) {
            if (exception.getCause() instanceof ConstraintViolationException e
                    && USERNAME_UNIQUE_INDEX.equals(e.getConstraintName())) {
                throw new UsernameAlreadyTakenException("Username %s is already taken".formatted(username));
            }

            throw exception;
        }
    }
}

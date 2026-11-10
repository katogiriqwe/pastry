package net.pastry.auth.account.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank
        @Pattern(
                regexp = "^[a-zA-Z0-9_]{3,32}$",
                message = "Username must be 3-32 characters: latin letters, digits or underscore"
        )
        String username,

        @NotBlank
        @Size(min = 8, max = 72)
        @Pattern(
                regexp = "^[\\x21-\\x7E]+$",
                message = "Password may contain only latin letters, digits and symbols"
        )
        String password
) {
}

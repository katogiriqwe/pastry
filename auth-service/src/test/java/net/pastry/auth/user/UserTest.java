package net.pastry.auth.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserTest {
    @Test
    void create_setsDefaultRoleUser() {
        var user = User.create("name", "hashed");
        assertThat(user.getRole()).isEqualTo(Role.USER);
    }

    @ParameterizedTest
    @CsvSource(nullValues = "null", value = {"vasya,null", "null, hashed", "null, null"})
    void create_withNullArgument_throwsNullPointerException(String name, String password) {
        assertThatThrownBy(() -> User.create(name, password))
                .isInstanceOf(NullPointerException.class);
    }
}

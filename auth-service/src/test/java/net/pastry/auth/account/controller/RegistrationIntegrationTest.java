package net.pastry.auth.account.controller;

import net.pastry.auth.AbstractIntegrationTest;
import net.pastry.auth.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RegistrationIntegrationTest extends AbstractIntegrationTest {
    private static final String REGISTER_URL = "/auth/register";

    @Test
    void register_withValidData_returns201AndSavesUser() throws Exception {
        postJson(REGISTER_URL, """
                {"username": "vasya", "password": "qwerty123"}
                """)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("vasya"))
                .andExpect(jsonPath("$.role").value("USER"))
                .andExpect(jsonPath("$.passwordHash").doesNotExist());

        var user = userRepository.findByLowerName("vasya").orElseThrow();
        assertThat(user.getPasswordHash()).startsWith("{bcrypt}");
    }

    @Test
    void register_withUsernameTakenIgnoringCase_returns409() throws Exception {
        userRepository.save(User.create("Vasya", "hashed"));

        postJson(REGISTER_URL, """
                {"username": "vasya", "password": "qwerty123"}
                """)
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value(409));

        assertThat(userRepository.count()).isEqualTo(1);
    }

    @ParameterizedTest(name = "{index}: invalid {1}")
    @CsvSource(delimiter = '|', textBlock = """
            {"username": "",      "password": "qwerty123"} | #/username
            {"username": "vasya", "password": "short"}     | #/password
            """)
    void register_withInvalidData_returns400(String json, String pointer) throws Exception {
        postJson(REGISTER_URL, json)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.errors[*].pointer", hasItem(pointer)));
    }
}
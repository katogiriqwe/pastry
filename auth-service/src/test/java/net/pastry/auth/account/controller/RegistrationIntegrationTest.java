package net.pastry.auth.account.controller;

import net.pastry.auth.TestcontainersConfiguration;
import net.pastry.auth.user.User;
import net.pastry.auth.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
public class RegistrationIntegrationTest {
    private static final String REGISTER_URL = "/auth/register";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void cleanDatabase() {
        userRepository.deleteAllInBatch();
    }

    @Test
    void register_withValidData_returns201AndSavesUser() throws Exception {
        register("""
                        {"username": "vasya", "password":  "qwerty123"}
                        """)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("vasya"))
                .andExpect(jsonPath("$.role").value("USER"));

        var user = userRepository.findByLowerName("vasya").orElseThrow();
        assertThat(user.getPasswordHash()).startsWith("{bcrypt}");
    }

    @Test
    void register_withUsernameTakenIgnoringCase_returns409() throws Exception {
        userRepository.save(User.create("Vasya", "hashed"));

        register("""
                        {"username": "vasya", "password": "qwerty123"}
                        """)
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON));

        assertThat(userRepository.count()).isEqualTo(1);
    }

    @ParameterizedTest(name = "{index}: invalid {1}")
    @CsvSource(delimiter = '|', textBlock = """
        {"username": "",        "password": "qwerty123"} | #/username
        {"username": "vasya",   "password": "short"}     | #/password
        """)
    void register_withInvalidData_returns400(String json, String pointer) throws Exception {
        register(json)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.errors[*].pointer", hasItem(pointer)));
    }

    private ResultActions register(String json) throws Exception {
        return mockMvc.perform(post(REGISTER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));
    }
}

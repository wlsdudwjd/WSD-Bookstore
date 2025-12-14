package com.example.bookstore.auth;

import com.example.bookstore.user.entity.Gender;
import com.example.bookstore.user.entity.Role;
import com.example.bookstore.user.entity.User;
import com.example.bookstore.user.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class AuthIntegrationTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ObjectMapper objectMapper;

    private String email;
    private final String password = "P@ssw0rd1!";

    @BeforeEach
    void setUp() {
        email = "user-" + UUID.randomUUID() + "@test.com";
    }

    @Test
    @DisplayName("회원가입 후 로그인까지 성공한다")
    void signupThenLogin() throws Exception {
        String signupBody = """
                {
                  "email": "%s",
                  "password": "%s",
                  "name": "tester",
                  "phoneNumber": "01012345678",
                  "address": "Seoul",
                  "gender": "MALE",
                  "birthday": "1990-01-01"
                }
                """.formatted(email, password);

        webTestClient.post()
                .uri("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(signupBody)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.data.email").isEqualTo(email);

        String loginBody = """
                {
                  "email": "%s",
                  "password": "%s"
                }
                """.formatted(email, password);

        webTestClient.post()
                .uri("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(loginBody)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.data.accessToken").isNotEmpty()
                .jsonPath("$.data.refreshToken").isNotEmpty();
    }

    @Test
    @DisplayName("잘못된 비밀번호는 401을 반환한다")
    void loginFailsWithWrongPassword() throws Exception {
        User user = User.createUser(
                email,
                passwordEncoder.encode(password),
                "tester",
                "01012345678",
                "Seoul",
                Gender.MALE,
                LocalDate.of(1990, 1, 1),
                Role.USER
        );
        userRepository.save(user);

        String wrongLogin = """
                {
                  "email": "%s",
                  "password": "wrong-password"
                }
                """.formatted(email);

        webTestClient.post()
                .uri("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(wrongLogin)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    @DisplayName("Refresh 토큰으로 Access 토큰을 재발급한다")
    void refreshToken() throws Exception {
        String signupBody = """
                {
                  "email": "%s",
                  "password": "%s",
                  "name": "tester",
                  "phoneNumber": "01012345678",
                  "address": "Seoul",
                  "gender": "MALE",
                  "birthday": "1990-01-01"
                }
                """.formatted(email, password);

        webTestClient.post()
                .uri("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(signupBody)
                .exchange()
                .expectStatus().isOk();

        String loginBody = """
                {
                  "email": "%s",
                  "password": "%s"
                }
                """.formatted(email, password);

        JsonNode loginJson = objectMapper.readTree(
                webTestClient.post()
                        .uri("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(loginBody)
                        .exchange()
                        .expectStatus().isOk()
                        .expectBody()
                        .returnResult()
                        .getResponseBodyContent()
        );
        String refreshToken = loginJson.path("data").path("refreshToken").asText();

        webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("/auth/refresh")
                        .queryParam("refreshToken", refreshToken)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.data.accessToken").isNotEmpty()
                .jsonPath("$.data.refreshToken").isNotEmpty();
    }

    @Test
    @DisplayName("Health 엔드포인트는 200 OK를 반환한다")
    void healthCheck() throws Exception {
        webTestClient.get()
                .uri("/health")
                .exchange()
                .expectStatus().isOk();
    }
}

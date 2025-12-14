package com.example.bookstore.auth;

import com.example.bookstore.auth.dto.LoginResponse;
import com.example.bookstore.auth.dto.SignupResponse;
import com.example.bookstore.auth.dto.TokenResponse;
import com.example.bookstore.common.api.ApiResponse;
import com.example.bookstore.support.IntegrationTestSupport;
import com.example.bookstore.user.entity.Gender;
import com.example.bookstore.user.entity.Role;
import com.example.bookstore.user.entity.User;
import com.example.bookstore.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Transactional
class AuthIntegrationTest extends IntegrationTestSupport {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    protected final RestTemplate client = this.restTemplate;

    private String email;
    private final String password = "P@ssw0rd1!";

    @BeforeEach
    void setUp() {
        email = "user-" + UUID.randomUUID() + "@test.com";
    }

    @Test
    @DisplayName("회원가입 후 로그인까지 성공한다")
    void signupThenLogin() {
        Map<String, Object> signupBody = Map.of(
                "email", email,
                "password", password,
                "name", "tester",
                "phoneNumber", "01012345678",
                "address", "Seoul",
                "gender", "MALE",
                "birthday", "1990-01-01"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> signupRequest = new HttpEntity<>(signupBody, headers);
        ResponseEntity<ApiResponse<SignupResponse>> signupRes = client.exchange(
                url("/auth/signup"),
                HttpMethod.POST,
                signupRequest,
                new ParameterizedTypeReference<>() {}
        );

        assertThat(signupRes.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(userRepository.findByEmail(email)).isPresent();

        HttpEntity<Map<String, Object>> loginRequest = new HttpEntity<>(
                Map.of("email", email, "password", password),
                headers
        );
        ResponseEntity<ApiResponse<LoginResponse>> loginRes = client.exchange(
                url("/auth/login"),
                HttpMethod.POST,
                loginRequest,
                new ParameterizedTypeReference<>() {}
        );

        assertThat(loginRes.getStatusCode()).isEqualTo(HttpStatus.OK);
        LoginResponse loginData = loginRes.getBody().getData();
        assertThat(loginData.accessToken()).isNotBlank();
        assertThat(loginData.refreshToken()).isNotBlank();
    }

    @Test
    @DisplayName("잘못된 비밀번호는 401을 반환한다")
    void loginFailsWithWrongPassword() {
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

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> wrongLogin = new HttpEntity<>(
                Map.of("email", email, "password", "wrong-password"),
                headers
        );

        ResponseEntity<String> res = client.postForEntity(url("/auth/login"), wrongLogin, String.class);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("Refresh 토큰으로 Access 토큰을 재발급한다")
    void refreshToken() {
        Map<String, Object> signupBody = Map.of(
                "email", email,
                "password", password,
                "name", "tester",
                "phoneNumber", "01012345678",
                "address", "Seoul",
                "gender", "MALE",
                "birthday", "1990-01-01"
        );
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        client.postForEntity(url("/auth/signup"), new HttpEntity<>(signupBody, headers), String.class);

        ResponseEntity<ApiResponse<LoginResponse>> loginRes = client.exchange(
                url("/auth/login"),
                HttpMethod.POST,
                new HttpEntity<>(Map.of("email", email, "password", password), headers),
                new ParameterizedTypeReference<>() {}
        );

        String refreshToken = loginRes.getBody().getData().refreshToken();

        ResponseEntity<ApiResponse<TokenResponse>> refreshRes = client.exchange(
                url("/auth/refresh?refreshToken=" + refreshToken),
                HttpMethod.POST,
                null,
                new ParameterizedTypeReference<>() {}
        );

        assertThat(refreshRes.getStatusCode()).isEqualTo(HttpStatus.OK);
        TokenResponse tokenData = refreshRes.getBody().getData();
        assertThat(tokenData.accessToken()).isNotBlank();
    }

    @Test
    @DisplayName("Health 엔드포인트는 200 OK를 반환한다")
    void healthCheck() {
        ResponseEntity<String> res = client.getForEntity(url("/health"), String.class);
        assertThat(res.getStatusCode().is2xxSuccessful()).isTrue();
    }
}

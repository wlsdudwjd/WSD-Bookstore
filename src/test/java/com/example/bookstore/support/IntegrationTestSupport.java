package com.example.bookstore.support;

import com.example.bookstore.auth.jwt.JwtProvider;
import com.example.bookstore.user.entity.Gender;
import com.example.bookstore.user.entity.Role;
import com.example.bookstore.user.entity.User;
import com.example.bookstore.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public abstract class IntegrationTestSupport {

    protected final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected JwtProvider jwtProvider;

    protected String url(String path) {
        return "http://localhost:18080" + path;
    }

    protected HttpHeaders authHeader(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return headers;
    }

    protected String createUserToken(Role role) {
        String email = role.name().toLowerCase() + "-" + UUID.randomUUID() + "@test.com";
        User user = User.createUser(
                email,
                passwordEncoder.encode("password123"),
                "tester",
                "01012341234",
                "Seoul",
                Gender.MALE,
                LocalDate.of(1990, 1, 1),
                role
        );
        userRepository.save(user);
        return jwtProvider.createAccessToken(user.getUserId(), user.getEmail(), user.getRole().name());
    }
}

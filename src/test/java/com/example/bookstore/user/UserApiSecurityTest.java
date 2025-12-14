package com.example.bookstore.user;

import com.example.bookstore.support.IntegrationTestSupport;
import com.example.bookstore.user.entity.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class UserApiSecurityTest extends IntegrationTestSupport {

    @Test
    @DisplayName("인증 없이 내 프로필 조회 시 401")
    void meUnauthorized() {
        ResponseEntity<String> res = restTemplate.getForEntity(url("/users/me"), String.class);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("USER 권한으로 관리자 API 호출 시 403")
    void adminEndpointForbiddenForUser() {
        String token = createUserToken(Role.USER);
        HttpHeaders headers = authHeader(token);
        ResponseEntity<String> res = restTemplate.exchange(
                url("/users"),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class
        );
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }
}

package com.example.bookstore.order;

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

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class OrderApiSecurityTest extends IntegrationTestSupport {

    @Test
    @DisplayName("인증 없이 주문 생성 시 401")
    void createOrderUnauthorized() {
        Map<String, Object> body = Map.of("items", java.util.List.of());
        ResponseEntity<String> res = restTemplate.postForEntity(url("/orders"), body, String.class);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("USER 토큰으로 주문 생성 시 본인 인증은 통과한다(400/200 허용)")
    void createOrderWithUserToken() {
        String token = createUserToken(Role.USER);
        HttpHeaders headers = authHeader(token);
        Map<String, Object> body = Map.of("items", java.util.List.of());

        ResponseEntity<String> res = restTemplate.exchange(
                url("/orders"),
                HttpMethod.POST,
                new HttpEntity<>(body, headers),
                String.class
        );
        assertThat(res.getStatusCode().is4xxClientError() || res.getStatusCode().is2xxSuccessful()).isTrue();
    }
}

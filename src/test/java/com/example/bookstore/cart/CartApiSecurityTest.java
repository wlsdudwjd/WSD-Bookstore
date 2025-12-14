package com.example.bookstore.cart;

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
class CartApiSecurityTest extends IntegrationTestSupport {

    @Test
    @DisplayName("인증 없이 장바구니 조회 시 401")
    void cartUnauthorized() {
        ResponseEntity<String> res = restTemplate.getForEntity(url("/cart"), String.class);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("USER 토큰으로 장바구니 조회는 200 또는 204를 허용한다")
    void cartWithUserToken() {
        String token = createUserToken(Role.USER);
        HttpHeaders headers = authHeader(token);
        ResponseEntity<String> res = restTemplate.exchange(
                url("/cart"),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class
        );
        assertThat(res.getStatusCode().is2xxSuccessful()).isTrue();
    }
}

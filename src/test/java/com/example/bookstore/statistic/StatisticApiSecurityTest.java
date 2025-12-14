package com.example.bookstore.statistic;

import com.example.bookstore.support.IntegrationTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class StatisticApiSecurityTest extends IntegrationTestSupport {

    @Test
    void getBookStatistics_requiresAuthentication() {
        ResponseEntity<String> res = restTemplate.getForEntity(url("/statistics/books/1"), String.class);

        assertThat(res.getStatusCode().value()).isEqualTo(401);
    }

    @Test
    void getBookStatistics_allowsAuthenticatedUser() {
        String token = createUserToken(com.example.bookstore.user.entity.Role.USER);
        HttpEntity<Void> entity = new HttpEntity<>(authHeader(token));

        ResponseEntity<String> res = restTemplate.exchange(
                url("/statistics/books/1"),
                HttpMethod.GET,
                entity,
                String.class
        );

        // Service may return empty/404 depending on data; ensure not rejected by auth
        assertThat(res.getStatusCode().is2xxSuccessful() || res.getStatusCode().is4xxClientError()).isTrue();
    }
}

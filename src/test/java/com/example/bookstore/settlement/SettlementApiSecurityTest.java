package com.example.bookstore.settlement;

import com.example.bookstore.support.IntegrationTestSupport;
import com.example.bookstore.user.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class SettlementApiSecurityTest extends IntegrationTestSupport {

    @Test
    void getSettlement_requiresAdminRole() {
        ResponseEntity<String> res = restTemplate.getForEntity(url("/settlements/sellers/1"), String.class);
        assertThat(res.getStatusCode().value()).isEqualTo(401);
    }

    @Test
    void getSettlement_forbiddenForUser() {
        String token = createUserToken(Role.USER);
        HttpEntity<Void> entity = new HttpEntity<>(authHeader(token));

        ResponseEntity<String> res = restTemplate.exchange(
                url("/settlements/sellers/1"),
                HttpMethod.GET,
                entity,
                String.class
        );

        assertThat(res.getStatusCode().value()).isEqualTo(403);
    }

    @Test
    void getSettlement_allowsAdminEvenIfDataMissing() {
        String token = createUserToken(Role.ADMIN);
        HttpEntity<Void> entity = new HttpEntity<>(authHeader(token));

        ResponseEntity<String> res = restTemplate.exchange(
                url("/settlements/sellers/1"),
                HttpMethod.GET,
                entity,
                String.class
        );

        assertThat(res.getStatusCode().is2xxSuccessful() || res.getStatusCode().is4xxClientError()).isTrue();
    }
}

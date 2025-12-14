package com.example.bookstore.discount;

import com.example.bookstore.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class DiscountApiSecurityTest extends IntegrationTestSupport {

    @Test
    @DisplayName("인증 없이 할인 목록 조회 시 401")
    void discountsUnauthorized() {
        ResponseEntity<String> res = restTemplate.getForEntity(url("/discounts"), String.class);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}

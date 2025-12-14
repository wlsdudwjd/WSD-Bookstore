package com.example.bookstore.review;

import com.example.bookstore.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class ReviewApiSecurityTest extends IntegrationTestSupport {

    @Test
    @DisplayName("인증 없이 리뷰 등록 시 401")
    void createReviewUnauthorized() {
        ResponseEntity<String> res = restTemplate.postForEntity(url("/reviews"), java.util.Map.of(), String.class);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}

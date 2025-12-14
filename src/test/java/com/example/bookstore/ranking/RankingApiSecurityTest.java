package com.example.bookstore.ranking;

import com.example.bookstore.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class RankingApiSecurityTest extends IntegrationTestSupport {

    @Test
    @DisplayName("인증 없이 랭킹 조회 시 401")
    void rankingUnauthorized() {
        ResponseEntity<String> res = restTemplate.getForEntity(url("/ranking"), String.class);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}

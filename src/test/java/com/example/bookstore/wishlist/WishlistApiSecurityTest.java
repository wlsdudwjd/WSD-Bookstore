package com.example.bookstore.wishlist;

import com.example.bookstore.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class WishlistApiSecurityTest extends IntegrationTestSupport {

    @Test
    @DisplayName("인증 없이 위시리스트 조회 시 401")
    void wishlistUnauthorized() {
        ResponseEntity<String> res = restTemplate.getForEntity(url("/wishlist"), String.class);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}

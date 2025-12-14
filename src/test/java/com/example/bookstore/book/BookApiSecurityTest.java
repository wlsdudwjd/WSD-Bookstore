package com.example.bookstore.book;

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
class BookApiSecurityTest extends IntegrationTestSupport {

    @Test
    @DisplayName("인증 없이 도서 등록 시 401을 반환한다")
    void createBookUnauthorized() {
        Map<String, Object> body = Map.of(
                "title", "test",
                "author", "author",
                "publisher", "pub",
                "isbn", "1234567890",
                "price", 1000,
                "publicationDate", "2025-01-01",
                "summary", "summary",
                "sellerId", 1
        );

        ResponseEntity<String> res = restTemplate.postForEntity(url("/books"), body, String.class);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("일반 사용자 토큰으로 도서 등록 시 403을 반환한다")
    void createBookForbiddenForUser() {
        String token = createUserToken(Role.USER);
        HttpHeaders headers = authHeader(token);
        Map<String, Object> body = Map.of(
                "title", "test",
                "author", "author",
                "publisher", "pub",
                "isbn", "1234567890123",
                "price", 1000,
                "publicationDate", "2025-01-01",
                "summary", "summary",
                "sellerId", 1
        );

        ResponseEntity<String> res = restTemplate.exchange(
                url("/books"),
                HttpMethod.POST,
                new HttpEntity<>(body, headers),
                String.class
        );
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }
}

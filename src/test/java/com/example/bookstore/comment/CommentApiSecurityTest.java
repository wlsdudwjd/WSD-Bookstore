package com.example.bookstore.comment;

import com.example.bookstore.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class CommentApiSecurityTest extends IntegrationTestSupport {

    @Test
    @DisplayName("인증 없이 댓글 등록 시 401")
    void createCommentUnauthorized() {
        ResponseEntity<String> res = restTemplate.postForEntity(url("/reviews/1/comments"), java.util.Map.of(), String.class);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}

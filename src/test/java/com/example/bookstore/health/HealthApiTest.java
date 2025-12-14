package com.example.bookstore.health;

import com.example.bookstore.support.IntegrationTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class HealthApiTest extends IntegrationTestSupport {

    @Test
    void health_isPublic() {
        ResponseEntity<String> res = restTemplate.getForEntity(url("/health"), String.class);

        assertThat(res.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(res.getBody()).contains("health ok");
    }
}

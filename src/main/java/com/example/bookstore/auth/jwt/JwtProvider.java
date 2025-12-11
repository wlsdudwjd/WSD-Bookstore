package com.example.bookstore.auth.jwt;

import com.example.bookstore.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;   // ★ 여기 중요!
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtConfig jwtConfig;
    private final JwtUserDetailsService userDetailsService;

    // ★ Key → SecretKey 로 변경
    private SecretKey key;

    @PostConstruct
    void init() {
        // 시크릿 문자열을 이용해 HMAC 키 생성
        this.key = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public String createAccessToken(String email, String role) {
        return createToken(email, role, jwtConfig.getAccessTokenExpirationMillis());
    }

    public String createRefreshToken(String email, String role) {
        return createToken(email, role, jwtConfig.getRefreshTokenExpirationMillis());
    }

    private String createToken(String email, String role, long validityMillis) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + validityMillis);

        return Jwts.builder()
                .subject(email)
                .claim("role", role)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key)   // SecretKey 사용
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)   // 이제 타입 맞음
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getEmail(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    public Authentication getAuthentication(String token) {
        String email = getEmail(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
    }
}
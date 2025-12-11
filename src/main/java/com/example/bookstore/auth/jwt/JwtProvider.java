package com.example.bookstore.auth.jwt;

import com.example.bookstore.config.JwtConfig;
import com.example.bookstore.user.entity.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collections;
import java.util.Date;

@Component
public class JwtProvider {

    private static final String CLAIM_ROLE = "role";

    private final JwtConfig jwtConfig;
    private final Key key;

    public JwtProvider(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(toBase64(jwtConfig.getSecret())));
    }

    private String toBase64(String secret) {
        // secret이 이미 base64가 아니라고 가정하고 간단히 encoding
        return java.util.Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public String generateAccessToken(String email, Role role) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtConfig.getAccessTokenExpirationMs());

        return Jwts.builder()
                .setSubject(email)
                .claim(CLAIM_ROLE, role.name())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String email, Role role) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtConfig.getRefreshTokenExpirationMs());

        return Jwts.builder()
                .setSubject(email)
                .claim(CLAIM_ROLE, role.name())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        String email = claims.getSubject();
        String roleName = claims.get(CLAIM_ROLE, String.class);

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roleName);
        return new UsernamePasswordAuthenticationToken(email, token, Collections.singletonList(authority));
    }
}
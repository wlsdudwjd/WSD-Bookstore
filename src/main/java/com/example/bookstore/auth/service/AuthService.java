package com.example.bookstore.auth.service;

import com.example.bookstore.auth.dto.LoginRequest;
import com.example.bookstore.auth.dto.LoginResponse;
import com.example.bookstore.auth.dto.SignupRequest;
import com.example.bookstore.auth.dto.TokenResponse;
import com.example.bookstore.auth.jwt.JwtProvider;
import com.example.bookstore.common.exception.CustomException;
import com.example.bookstore.common.exception.ErrorCode;
import com.example.bookstore.user.entity.Role;
import com.example.bookstore.user.entity.User;
import com.example.bookstore.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public void signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new CustomException(
                    ErrorCode.DUPLICATE_RESOURCE,
                    Map.of("email", request.email())
            );
        }

        if (userRepository.existsByPhoneNumber(request.phoneNumber())) {
            throw new CustomException(
                    ErrorCode.DUPLICATE_RESOURCE,
                    Map.of("phoneNumber", request.phoneNumber())
            );
        }

        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .name(request.name())
                .phoneNumber(request.phoneNumber())
                .address(request.address())
                .gender(request.gender())
                .birthday(request.birthday())
                .role(Role.ROLE_USER)
                .build();

        userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new CustomException(
                        ErrorCode.USER_NOT_FOUND,
                        Map.of("email", request.email())
                ));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new CustomException(
                    ErrorCode.UNAUTHORIZED,
                    Map.of("reason", "wrong password")
            );
        }

        String accessToken = jwtProvider.generateAccessToken(user.getEmail(), user.getRole());
        String refreshToken = jwtProvider.generateRefreshToken(user.getEmail(), user.getRole());

        return new LoginResponse(accessToken, refreshToken);
    }

    public TokenResponse refresh(String refreshToken) {
        if (!jwtProvider.validateToken(refreshToken)) {
            throw new CustomException(
                    ErrorCode.TOKEN_EXPIRED,
                    Map.of("token", "invalid or expired")
            );
        }

        var auth = jwtProvider.getAuthentication(refreshToken);
        String email = auth.getName();
        String roleName = auth.getAuthorities().iterator().next().getAuthority();
        Role role = Role.valueOf(roleName);

        String newAccessToken = jwtProvider.generateAccessToken(email, role);
        return new TokenResponse(newAccessToken);
    }
}
package com.example.bookstore.auth.service;

import com.example.bookstore.auth.dto.LoginRequest;
import com.example.bookstore.auth.dto.LoginResponse;
import com.example.bookstore.auth.dto.SignupRequest;
import com.example.bookstore.auth.dto.TokenResponse;
import com.example.bookstore.auth.jwt.JwtProvider;
import com.example.bookstore.common.exception.CustomException;
import com.example.bookstore.common.exception.ErrorCode;
import com.example.bookstore.user.entity.User;
import com.example.bookstore.user.entity.Role;
import com.example.bookstore.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    // ================================
    // 회원가입
    // ================================
    public void signup(SignupRequest request) {

        // ★ record면 request.email() 형식으로 접근
        if (userRepository.existsByEmail(request.email())) {
            throw new CustomException(
                    ErrorCode.DUPLICATE_RESOURCE,
                    Map.of("email", "이미 사용 중인 이메일입니다.")
            );
        }

        if (userRepository.existsByPhoneNumber(request.phoneNumber())) {
            throw new CustomException(
                    ErrorCode.DUPLICATE_RESOURCE,
                    Map.of("phoneNumber", "이미 사용 중인 전화번호입니다.")
            );
        }

        // ★ Role.USER 부분은 너의 Role enum에 맞게 수정 가능
        User user = User.createUser(
                request.email(),
                passwordEncoder.encode(request.password()),
                request.name(),
                request.phoneNumber(),
                request.address(),
                request.gender(),
                request.birthday(),
                Role.USER      // <-- Role enum에 USER 없으면 존재하는 값으로 바꿔줘
        );

        userRepository.save(user);
    }

    // ================================
    // 로그인
    // ================================
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new CustomException(
                        ErrorCode.USER_NOT_FOUND,
                        Map.of("email", "사용자를 찾을 수 없습니다.")
                ));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new CustomException(
                    ErrorCode.UNAUTHORIZED,
                    Map.of("password", "비밀번호가 일치하지 않습니다.")
            );
        }

        String roleName = user.getRole().name();
        String accessToken = jwtProvider.createAccessToken(user.getEmail(), roleName);
        String refreshToken = jwtProvider.createRefreshToken(user.getEmail(), roleName);

        return new LoginResponse(accessToken, refreshToken);
    }

    // ================================
    // 토큰 재발급
    // ================================
    public TokenResponse refresh(String refreshToken) {

        if (!jwtProvider.validateToken(refreshToken)) {
            throw new CustomException(
                    ErrorCode.TOKEN_EXPIRED,
                    Map.of("refreshToken", "Refresh Token이 유효하지 않습니다.")
            );
        }

        String email = jwtProvider.getEmail(refreshToken);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(
                        ErrorCode.USER_NOT_FOUND,
                        Map.of("email", "사용자를 찾을 수 없습니다.")
                ));

        String newAccessToken =
                jwtProvider.createAccessToken(user.getEmail(), user.getRole().name());

        return new TokenResponse(newAccessToken);
    }

    // ================================
    // 로그아웃 (필요시 블랙리스트 처리 등)
    // ================================
    public void logout(String refreshToken) {
        // 과제 요구사항 최소 기준에선 비워둬도 무방
        // 나중에 Redis 등에 refreshToken 블랙리스트 넣는 로직 구현 가능
    }
}
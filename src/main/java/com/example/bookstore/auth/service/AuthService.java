package com.example.bookstore.auth.service;

import com.example.bookstore.auth.dto.LoginRequest;
import com.example.bookstore.auth.dto.LoginResponse;
import com.example.bookstore.auth.dto.SignupRequest;
import com.example.bookstore.auth.dto.SignupResponse;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional
    public SignupResponse signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(
                    ErrorCode.DUPLICATE_RESOURCE,
                    Map.of("email", "이미 사용 중인 이메일입니다.")
            );
        }
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new CustomException(
                    ErrorCode.DUPLICATE_RESOURCE,
                    Map.of("phoneNumber", "이미 사용 중인 전화번호입니다.")
            );
        }

        User user = User.createUser(
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getName(),
                request.getPhoneNumber(),
                request.getAddress(),
                request.getGender(),     // Gender enum
                request.getBirthday(),
                Role.USER                // 너 Role enum 값에 맞게 USER / ROLE_USER 중 골라
        );

        User saved = userRepository.save(user);
        return new SignupResponse(saved.getUserId(), saved.getCreatedAt());
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException(
                        ErrorCode.USER_NOT_FOUND,
                        Map.of("email", "사용자를 찾을 수 없습니다.")
                ));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(
                    ErrorCode.UNAUTHORIZED,
                    Map.of("password", "비밀번호가 일치하지 않습니다.")
            );
        }

        String accessToken = jwtProvider.createAccessToken(
                user.getUserId(),
                user.getEmail(),
                user.getRole().name()
        );
        String refreshToken = jwtProvider.createRefreshToken(
                user.getUserId(),
                user.getEmail(),
                user.getRole().name()
        );

        return new LoginResponse(
                accessToken,
                refreshToken,
                new LoginResponse.UserInfo(
                        user.getUserId(),
                        user.getName(),
                        user.getRole().name().toLowerCase()
                )
        );
    }

    @Transactional
    public TokenResponse refresh(String refreshToken) {
        if (!jwtProvider.validateToken(refreshToken)) {
            throw new CustomException(
                    ErrorCode.TOKEN_EXPIRED,
                    Map.of("token", "Refresh Token이 유효하지 않습니다.")
            );
        }

        Long userId = jwtProvider.getUserId(refreshToken);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(
                        ErrorCode.USER_NOT_FOUND,
                        Map.of("userId", "사용자를 찾을 수 없습니다.")
                ));

        String newAccessToken = jwtProvider.createAccessToken(
                user.getUserId(),
                user.getEmail(),
                user.getRole().name()
        );

        return new TokenResponse(newAccessToken);
    }

    @Transactional(readOnly = true)
    public void logout(String refreshToken) {
        // 실제 환경에서는 refreshToken을 블랙리스트에 저장하거나 DB에 보관 후 무효화해야 한다.
        // 현재는 별도 저장소가 없으므로 유효성만 확인한 뒤 성공 응답을 내려준다.
        if (refreshToken != null && !refreshToken.isBlank() && !jwtProvider.validateToken(refreshToken)) {
            throw new CustomException(
                    ErrorCode.INVALID_REQUEST,
                    Map.of("refreshToken", "유효하지 않은 토큰입니다.")
            );
        }
    }
}

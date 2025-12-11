package com.example.bookstore.auth.controller;

import com.example.bookstore.auth.dto.LoginRequest;
import com.example.bookstore.auth.dto.LoginResponse;
import com.example.bookstore.auth.dto.SignupRequest;
import com.example.bookstore.auth.dto.TokenResponse;
import com.example.bookstore.auth.service.AuthService;
import com.example.bookstore.common.api.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // ================================
    // 회원가입
    // ================================
    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "회원가입 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "중복된 이메일 또는 전화번호"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public ApiResponse<Void> signup(@RequestBody @Valid SignupRequest request) {
        authService.signup(request);
        return ApiResponse.ok(null);
    }


    @PostMapping("/login")
    @Operation(summary = "로그인", description = "이메일/비밀번호로 로그인하고 JWT 토큰을 발급합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "로그인 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "비밀번호 불일치"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "유저 없음")
    })
    public ApiResponse<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ApiResponse.ok(response);
    }

    @PostMapping("/refresh")
    @Operation(summary = "토큰 재발급", description = "Refresh Token을 통해 Access Token을 재발급합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "재발급 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Refresh Token 만료/위조"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "토큰 형식 오류")
    })
    public ApiResponse<TokenResponse> refresh(@RequestParam("refreshToken") String refreshToken) {
        TokenResponse response = authService.refresh(refreshToken);
        return ApiResponse.ok(response);
    }
}
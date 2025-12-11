package com.example.bookstore.user.controller;

import com.example.bookstore.common.api.ApiResponse;
import com.example.bookstore.common.api.PageResponse;
import com.example.bookstore.user.dto.UserProfileResponse;
import com.example.bookstore.user.dto.UserResponse;
import com.example.bookstore.user.dto.UserUpdateRequest;
import com.example.bookstore.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @Operation(summary = "내 프로필 조회", description = "현재 로그인한 사용자의 프로필 정보를 반환한다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "조회 성공"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401", description = "인증 필요"
            )
    })
    public ApiResponse<UserProfileResponse> getMyProfile() {
        UserProfileResponse response = userService.getMyProfile();
        return ApiResponse.ok(response);
    }

    @PatchMapping("/me")
    @Operation(summary = "내 프로필 수정", description = "이름, 전화번호, 주소를 수정한다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "수정 성공"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400", description = "요청 값 검증 실패"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401", description = "인증 필요"
            )
    })
    public ApiResponse<Void> updateMyProfile(@RequestBody @Valid UserUpdateRequest request) {
        userService.updateMyProfile(request);
        return ApiResponse.ok(null);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "전체 유저 목록 조회 (ADMIN)",
            description = "페이지네이션/정렬이 가능한 유저 목록 조회 API"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "조회 성공"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401", description = "인증 필요"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403", description = "권한 없음 (ADMIN 아님)"
            )
    })
    public ApiResponse<PageResponse<UserResponse>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String sort
    ) {
        PageResponse<UserResponse> response = userService.getUsers(page, size, sort);
        return ApiResponse.ok(response);
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "유저 비활성화 (ADMIN)",
            description = "지정한 유저를 비활성 상태로 변경한다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "비활성화 성공"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401", description = "인증 필요"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403", description = "권한 없음"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404", description = "유저 없음"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409", description = "이미 비활성화된 유저"
            )
    })
    public ApiResponse<Void> deactivateUser(@PathVariable("id") Long userId) {
        userService.deactivateUser(userId);
        return ApiResponse.ok(null);
    }
}
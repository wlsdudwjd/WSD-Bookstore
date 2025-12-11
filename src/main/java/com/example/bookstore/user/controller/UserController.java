package com.example.bookstore.user.controller;

import com.example.bookstore.common.api.ApiResponse;
import com.example.bookstore.common.api.PageResponse;
import com.example.bookstore.user.dto.UserProfileResponse;
import com.example.bookstore.user.dto.UserResponse;
import com.example.bookstore.user.dto.UserUpdateRequest;
import com.example.bookstore.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "유저 정보 API")
public class UserController {

    private final UserService userService;


    @GetMapping("/me")
    @Operation(summary = "내 정보 조회", description = "현재 로그인한 사용자의 프로필 정보를 조회합니다.")
    public ApiResponse<UserProfileResponse> getMyProfile() {
        return ApiResponse.success("조회 성공", userService.getMyProfile());
    }

    @PatchMapping("/me")
    @Operation(summary = "내 정보 수정", description = "현재 로그인한 사용자의 이름/전화번호/주소를 수정합니다.")
    public ApiResponse<UserProfileResponse> updateMyProfile(
            @RequestBody @Valid UserUpdateRequest request
    ) {
        return ApiResponse.success("프로필이 수정되었습니다.", userService.updateMyProfile(request));
    }

    @DeleteMapping("/me")
    @Operation(summary = "계정 삭제", description = "현재 로그인한 사용자의 계정을 삭제(비활성화)합니다.")
    public ApiResponse<Void> deleteMyAccount() {
        userService.deleteMyAccount();
        return ApiResponse.success("계정이 삭제되었습니다.");
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "유저 목록 조회 (ADMIN)", description = "관리자가 전체 유저 목록을 페이징하여 조회합니다.")
    public ApiResponse<PageResponse<UserResponse>> getUserList(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        var page = userService.getUserList(pageable);
        return ApiResponse.success("조회 성공", PageResponse.from(page));
    }


    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "유저 단건 조회 (ADMIN)", description = "관리자가 특정 유저 정보를 조회합니다.")
    public ApiResponse<UserResponse> getUserById(@PathVariable Long userId) {
        return ApiResponse.success("조회 성공", userService.getUserById(userId));
    }

    @PatchMapping("/{userId}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "유저 비활성화 (ADMIN)", description = "관리자가 유저를 비활성화 처리합니다.")
    public ApiResponse<Void> deactivateUser(@PathVariable Long userId) {
        userService.deactivateUser(userId);
        return ApiResponse.success("계정을 비활성화했습니다.");
    }
}

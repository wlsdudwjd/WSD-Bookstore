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

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // GET /users/me
    @GetMapping("/me")
    public ApiResponse<UserProfileResponse> getMyProfile() {
        UserProfileResponse response = userService.getMyProfile();
        return ApiResponse.ok(response);
    }

    // PATCH /users/me
    @PatchMapping("/me")
    public ApiResponse<Void> updateMyProfile(@RequestBody @Valid UserUpdateRequest request) {
        userService.updateMyProfile(request);
        return ApiResponse.ok(null);
    }

    // GET /users?page=0&size=20&sort=createdAt,DESC
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<PageResponse<UserResponse>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String sort
    ) {
        PageResponse<UserResponse> response = userService.getUsers(page, size, sort);
        return ApiResponse.ok(response);
    }

    // PATCH /users/{id}/deactivate
    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deactivateUser(@PathVariable("id") Long userId) {
        userService.deactivateUser(userId);
        return ApiResponse.ok(null);
    }
}
package com.example.bookstore.wishlist.controller;

import com.example.bookstore.common.api.ApiResponse;
import com.example.bookstore.common.util.SecurityUtil;
import com.example.bookstore.wishlist.dto.WishlistAddResponse;
import com.example.bookstore.wishlist.dto.WishlistListResponse;
import com.example.bookstore.wishlist.dto.WishlistRequest;
import com.example.bookstore.wishlist.service.WishlistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ApiResponse<WishlistAddResponse> add(@RequestBody @Valid WishlistRequest request) {
        Integer userId = SecurityUtil.getCurrentUserId();
        return ApiResponse.success("위시리스트에 추가되었습니다.", wishlistService.add(userId, request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ApiResponse<WishlistListResponse> getList() {
        Integer userId = SecurityUtil.getCurrentUserId();
        return ApiResponse.success("조회 성공", wishlistService.getList(userId));
    }

    @DeleteMapping("/{bookId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ApiResponse<Void> remove(@PathVariable Long bookId) {
        Integer userId = SecurityUtil.getCurrentUserId();
        wishlistService.remove(userId, bookId);
        return ApiResponse.success("위시리스트에서 삭제되었습니다.");
    }
}

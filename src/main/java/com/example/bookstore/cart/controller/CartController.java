package com.example.bookstore.cart.controller;

import com.example.bookstore.cart.dto.CartItemRequest;
import com.example.bookstore.cart.dto.CartResponse;
import com.example.bookstore.cart.service.CartService;
import com.example.bookstore.common.api.ApiResponse;
import com.example.bookstore.common.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@Tag(name = "Cart", description = "장바구니 API")
public class CartController {

    private final CartService cartService;

    // 내 장바구니 조회
    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "내 장바구니 조회", description = "로그인한 사용자의 장바구니를 조회합니다.")
    public ApiResponse<CartResponse> getMyCart() {
        Long userId = SecurityUtil.getCurrentUserId();
        return ApiResponse.ok(cartService.getMyCart(userId));
    }

    // 장바구니 담기 (수량 누적)
    @PostMapping("/items")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "장바구니 담기", description = "도서를 장바구니에 추가합니다. 이미 있으면 수량을 증가시킵니다.")
    public ApiResponse<CartResponse> addItem(@RequestBody @Valid CartItemRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        return ApiResponse.ok(cartService.addItem(userId, request));
    }

    // 수량 변경
    @PatchMapping("/items/{cartItemId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "장바구니 수량 변경", description = "장바구니 아이템의 수량을 변경합니다. 0 이하로 보내면 삭제됩니다.")
    public ApiResponse<CartResponse> updateItemQuantity(
            @PathVariable Long cartItemId,
            @RequestParam("quantity") Integer quantity
    ) {
        Long userId = SecurityUtil.getCurrentUserId();
        return ApiResponse.ok(cartService.updateItemQuantity(userId, cartItemId, quantity));
    }

    // 아이템 삭제
    @DeleteMapping("/items/{cartItemId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "장바구니 아이템 삭제", description = "특정 장바구니 아이템을 삭제합니다.")
    public ApiResponse<Void> removeItem(@PathVariable Long cartItemId) {
        Long userId = SecurityUtil.getCurrentUserId();
        cartService.removeItem(userId, cartItemId);
        return ApiResponse.noContent();
    }

    // 전체 비우기
    @DeleteMapping("/clear")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "장바구니 비우기", description = "장바구니를 모두 비웁니다.")
    public ApiResponse<Void> clearCart() {
        Long userId = SecurityUtil.getCurrentUserId();
        cartService.clearCart(userId);
        return ApiResponse.noContent();
    }
}
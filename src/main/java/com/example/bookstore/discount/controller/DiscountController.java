package com.example.bookstore.discount.controller;

import com.example.bookstore.common.api.ApiResponse;
import com.example.bookstore.discount.entity.Discount;
import com.example.bookstore.discount.service.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/discounts")
@RequiredArgsConstructor
public class DiscountController {

    private final DiscountService discountService;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ApiResponse<List<Discount>> getDiscounts() {
        return ApiResponse.success("조회 성공", discountService.getDiscounts());
    }

    @GetMapping("/{discountId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ApiResponse<Discount> getDiscount(@PathVariable Long discountId) {
        return ApiResponse.success("조회 성공", discountService.getDiscount(discountId));
    }
}

package com.example.bookstore.review.controller;

import com.example.bookstore.common.api.ApiResponse;
import com.example.bookstore.review.dto.*;
import com.example.bookstore.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ApiResponse<ReviewResponse> create(@RequestBody @Valid ReviewCreateRequest request) {
        return ApiResponse.success("리뷰가 등록되었습니다.", reviewService.create(request));
    }

    @PatchMapping("/{reviewId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ApiResponse<ReviewUpdateResponse> update(
            @PathVariable Long reviewId,
            @RequestBody @Valid ReviewUpdateRequest request
    ) {
        return ApiResponse.success("리뷰가 수정되었습니다.",
                new ReviewUpdateResponse(reviewService.update(reviewId, request)));
    }

    @DeleteMapping("/{reviewId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ApiResponse<Void> delete(@PathVariable Long reviewId) {
        reviewService.delete(reviewId);
        return ApiResponse.success("리뷰가 삭제되었습니다.");
    }

    @PostMapping("/{reviewId}/like")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ApiResponse<ReviewLikeResponse> like(@PathVariable Long reviewId) {
        int likeCount = reviewService.like(reviewId);
        return ApiResponse.success("좋아요가 등록되었습니다.", new ReviewLikeResponse(likeCount));
    }

    @DeleteMapping("/{reviewId}/like")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ApiResponse<ReviewLikeResponse> unlike(@PathVariable Long reviewId) {
        int likeCount = reviewService.unlike(reviewId);
        return ApiResponse.success("좋아요가 취소되었습니다.", new ReviewLikeResponse(likeCount));
    }
}

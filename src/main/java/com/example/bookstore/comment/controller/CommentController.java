package com.example.bookstore.comment.controller;

import com.example.bookstore.comment.dto.*;
import com.example.bookstore.comment.service.CommentService;
import com.example.bookstore.common.api.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/reviews/{reviewId}/comments")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ApiResponse<CommentCreateResponse> addComment(
            @PathVariable Long reviewId,
            @RequestBody @Valid CommentCreateRequest request
    ) {
        var response = commentService.addComment(reviewId, request.content(), request.parentCommentId());
        return ApiResponse.success("댓글이 등록되었습니다.", response);
    }

    @PatchMapping("/comments/{commentId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ApiResponse<CommentUpdateResponse> updateComment(
            @PathVariable Long commentId,
            @RequestBody @Valid CommentUpdateRequest request
    ) {
        var updatedAt = commentService.updateComment(commentId, request.content());
        return ApiResponse.success("댓글이 수정되었습니다.", new CommentUpdateResponse(updatedAt));
    }

    @DeleteMapping("/comments/{commentId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ApiResponse<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ApiResponse.success("댓글이 삭제되었습니다.");
    }

    @PostMapping("/comments/{commentId}/like")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ApiResponse<CommentLikeResponse> likeComment(@PathVariable Long commentId) {
        int likeCount = commentService.likeComment(commentId);
        return ApiResponse.success("좋아요가 등록되었습니다.", new CommentLikeResponse(likeCount));
    }

    @DeleteMapping("/comments/{commentId}/like")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ApiResponse<CommentLikeResponse> unlikeComment(@PathVariable Long commentId) {
        int likeCount = commentService.unlikeComment(commentId);
        return ApiResponse.success("좋아요가 취소되었습니다.", new CommentLikeResponse(likeCount));
    }
}

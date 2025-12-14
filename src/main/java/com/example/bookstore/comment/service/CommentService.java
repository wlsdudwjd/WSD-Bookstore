package com.example.bookstore.comment.service;

import com.example.bookstore.comment.dto.CommentDto;
import com.example.bookstore.comment.dto.CommentCreateResponse;
import com.example.bookstore.comment.entity.Comment;
import com.example.bookstore.comment.entity.CommentLike;
import com.example.bookstore.comment.entity.CommentLikeId;
import com.example.bookstore.comment.repository.CommentLikeRepository;
import com.example.bookstore.comment.repository.CommentRepository;
import com.example.bookstore.common.exception.CustomException;
import com.example.bookstore.common.exception.ErrorCode;
import com.example.bookstore.common.util.SecurityUtil;
import com.example.bookstore.review.entity.Review;
import com.example.bookstore.review.repository.ReviewRepository;
import com.example.bookstore.user.entity.User;
import com.example.bookstore.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentCreateResponse addComment(Long reviewId, String content, Long parentCommentId) {
        User user = getCurrentUser();
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        Map.of("reviewId", "리뷰를 찾을 수 없습니다.")
                ));

        Comment parent = null;
        if (parentCommentId != null) {
            parent = commentRepository.findById(parentCommentId)
                    .orElseThrow(() -> new CustomException(
                            ErrorCode.RESOURCE_NOT_FOUND,
                            Map.of("parentCommentId", "부모 댓글을 찾을 수 없습니다.")
                    ));
        }

        Comment comment = Comment.builder()
                .review(review)
                .user(user)
                .parent(parent)
                .content(content)
                .likeCount(0)
                .build();

        Comment saved = commentRepository.save(comment);
        return new CommentCreateResponse(saved.getCommentId(), saved.getCreatedAt());
    }

    @Transactional
    public LocalDateTime updateComment(Long commentId, String content) {
        Comment comment = getOwnComment(commentId);
        comment.updateContent(content);
        return comment.getUpdatedAt();
    }

    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = getOwnComment(commentId);
        commentRepository.delete(comment);
    }

    @Transactional
    public int likeComment(Long commentId) {
        User user = getCurrentUser();
        Comment comment = getComment(commentId);

        if (commentLikeRepository.findByUserAndComment(user, comment).isPresent()) {
            return comment.getLikeCount();
        }
        commentLikeRepository.save(
                CommentLike.builder()
                        .id(new CommentLikeId(user.getUserId(), comment.getCommentId()))
                        .user(user)
                        .comment(comment)
                        .build()
        );
        comment.increaseLike();
        return comment.getLikeCount();
    }

    @Transactional
    public int unlikeComment(Long commentId) {
        User user = getCurrentUser();
        Comment comment = getComment(commentId);
        commentLikeRepository.findByUserAndComment(user, comment)
                .ifPresent(cl -> {
                    commentLikeRepository.delete(cl);
                    comment.decreaseLike();
                });
        return comment.getLikeCount();
    }

    public List<CommentDto> getCommentsByReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        Map.of("reviewId", "리뷰를 찾을 수 없습니다.")
                ));
        return commentRepository.findAllByReview(review).stream()
                .map(c -> new CommentDto(c.getCommentId(), c.getReview().getReviewId(), c.getContent(), c.getLikeCount()))
                .toList();
    }

    private Comment getOwnComment(Long commentId) {
        Comment comment = getComment(commentId);
        Integer userId = SecurityUtil.getCurrentUserId();
        if (!comment.getUser().getUserId().equals(userId)) {
            throw new CustomException(
                    ErrorCode.FORBIDDEN,
                    Map.of("commentId", "해당 댓글을 수정/삭제할 수 없습니다.")
            );
        }
        return comment;
    }

    private Comment getComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        Map.of("commentId", "댓글을 찾을 수 없습니다.")
                ));
    }

    private User getCurrentUser() {
        Integer userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            throw new CustomException(
                    ErrorCode.UNAUTHORIZED,
                    Map.of("auth", "인증 정보가 없습니다.")
            );
        }
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(
                        ErrorCode.USER_NOT_FOUND,
                        Map.of("userId", "사용자를 찾을 수 없습니다.")
                ));
    }
}

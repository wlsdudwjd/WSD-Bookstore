package com.example.bookstore.review.service;

import com.example.bookstore.book.entity.Book;
import com.example.bookstore.book.repository.BookRepository;
import com.example.bookstore.common.exception.CustomException;
import com.example.bookstore.common.exception.ErrorCode;
import com.example.bookstore.common.util.SecurityUtil;
import com.example.bookstore.review.dto.ReviewCreateRequest;
import com.example.bookstore.review.dto.ReviewResponse;
import com.example.bookstore.review.dto.ReviewUpdateRequest;
import com.example.bookstore.review.entity.Review;
import com.example.bookstore.review.entity.ReviewLike;
import com.example.bookstore.review.entity.ReviewLikeId;
import com.example.bookstore.review.repository.ReviewLikeRepository;
import com.example.bookstore.review.repository.ReviewRepository;
import com.example.bookstore.user.entity.User;
import com.example.bookstore.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Transactional
    public ReviewResponse create(ReviewCreateRequest request) {
        User user = getCurrentUser();
        Book book = bookRepository.findById(request.bookId())
                .orElseThrow(() -> new CustomException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        Map.of("bookId", "도서를 찾을 수 없습니다.")
                ));

        reviewRepository.findByUserAndBook(user, book).ifPresent(r -> {
            throw new CustomException(
                    ErrorCode.STATE_CONFLICT,
                    Map.of("review", "이미 작성한 리뷰가 있습니다.")
            );
        });

        Review review = Review.builder()
                .user(user)
                .book(book)
                .comment(request.content())
                .content(request.content())
                .rating(request.rating())
                .likeCount(0)
                .build();

        Review saved = reviewRepository.save(review);
        return toResponse(saved);
    }

    @Transactional
    public LocalDateTime update(Long reviewId, ReviewUpdateRequest request) {
        Review review = getOwnReview(reviewId);
        review.update(request.content(), request.rating());
        return review.getUpdatedAt();
    }

    @Transactional
    public void delete(Long reviewId) {
        Review review = getOwnReview(reviewId);
        reviewRepository.delete(review);
    }

    @Transactional
    public int like(Long reviewId) {
        User user = getCurrentUser();
        Review review = getReview(reviewId);

        if (reviewLikeRepository.findByUserAndReview(user, review).isPresent()) {
            return review.getLikeCount();
        }

        reviewLikeRepository.save(
                ReviewLike.builder()
                        .id(new ReviewLikeId(user.getUserId(), review.getReviewId().intValue()))
                        .user(user)
                        .review(review)
                        .build()
        );
        review.increaseLike();
        return review.getLikeCount();
    }

    @Transactional
    public int unlike(Long reviewId) {
        User user = getCurrentUser();
        Review review = getReview(reviewId);

        reviewLikeRepository.findByUserAndReview(user, review)
                .ifPresent(like -> {
                    reviewLikeRepository.delete(like);
                    review.decreaseLike();
                });
        return review.getLikeCount();
    }

    private Review getOwnReview(Long reviewId) {
        Review review = getReview(reviewId);
        Integer currentUserId = SecurityUtil.getCurrentUserId();
        if (!review.getUser().getUserId().equals(currentUserId)) {
            throw new CustomException(
                    ErrorCode.FORBIDDEN,
                    Map.of("reviewId", "해당 리뷰를 수정/삭제할 수 없습니다.")
            );
        }
        return review;
    }

    private Review getReview(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        Map.of("reviewId", "리뷰를 찾을 수 없습니다.")
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

    private ReviewResponse toResponse(Review review) {
        return new ReviewResponse(
                review.getReviewId(),
                review.getBook().getBookId(),
                review.getContent(),
                review.getRating(),
                review.getLikeCount()
        );
    }
}

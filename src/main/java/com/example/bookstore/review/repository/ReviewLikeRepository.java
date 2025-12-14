package com.example.bookstore.review.repository;

import com.example.bookstore.review.entity.Review;
import com.example.bookstore.review.entity.ReviewLike;
import com.example.bookstore.review.entity.ReviewLikeId;
import com.example.bookstore.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, ReviewLikeId> {

    Optional<ReviewLike> findByUserAndReview(User user, Review review);
}

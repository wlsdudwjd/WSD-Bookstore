package com.example.bookstore.comment.repository;

import com.example.bookstore.comment.entity.Comment;
import com.example.bookstore.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByReview(Review review);
}

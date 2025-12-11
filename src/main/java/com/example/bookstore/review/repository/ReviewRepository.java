package com.example.bookstore.review.repository;

import com.example.bookstore.book.entity.Book;
import com.example.bookstore.review.entity.Review;
import com.example.bookstore.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByUserAndBook(User user, Book book);
}

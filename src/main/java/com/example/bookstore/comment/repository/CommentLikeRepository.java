package com.example.bookstore.comment.repository;

import com.example.bookstore.comment.entity.Comment;
import com.example.bookstore.comment.entity.CommentLike;
import com.example.bookstore.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    Optional<CommentLike> findByUserAndComment(User user, Comment comment);
}

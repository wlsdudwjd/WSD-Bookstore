package com.example.bookstore.review.entity;

import com.example.bookstore.book.entity.Book;
import com.example.bookstore.common.util.DateUtil;
import com.example.bookstore.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(
        name = "review",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_review_user_book", columnNames = {"user_id", "book_id"})
        },
        indexes = {
                @Index(name = "idx_review_book_id", columnList = "book_id"),
                @Index(name = "idx_review_user_id", columnList = "user_id")
        }
)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id", columnDefinition = "INT")
    private Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", nullable = false, columnDefinition = "INT")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, columnDefinition = "INT")
    private User user;

    @Column(name = "comment", nullable = false, columnDefinition = "TEXT")
    private String comment;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false)
    private Integer rating;

    @Column(nullable = false)
    private Integer likeCount;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = DateUtil.now();
        this.updatedAt = DateUtil.now();
        if (this.likeCount == null) {
            this.likeCount = 0;
        }
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = DateUtil.now();
    }

    public void update(String content, Integer rating) {
        if (content != null && !content.isBlank()) {
            this.content = content;
            this.comment = content;
        }
        if (rating != null) {
            this.rating = rating;
        }
    }

    public void increaseLike() {
        this.likeCount = (this.likeCount == null ? 0 : this.likeCount) + 1;
    }

    public void decreaseLike() {
        if (this.likeCount == null || this.likeCount <= 0) {
            this.likeCount = 0;
        } else {
            this.likeCount -= 1;
        }
    }
}

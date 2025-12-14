package com.example.bookstore.comment.entity;

import com.example.bookstore.common.util.DateUtil;
import com.example.bookstore.review.entity.Review;
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
        name = "comment",
        indexes = {
                @Index(name = "idx_comment_review_id", columnList = "review_id"),
                @Index(name = "idx_comment_user_id", columnList = "user_id")
        }
)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", columnDefinition = "INT")
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "review_id", nullable = false, columnDefinition = "INT")
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, columnDefinition = "INT")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id", columnDefinition = "INT")
    private Comment parent;

    @Column(nullable = false, length = 1000)
    private String content;

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

    public void updateContent(String content) {
        if (content != null && !content.isBlank()) {
            this.content = content;
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

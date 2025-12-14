package com.example.bookstore.review.entity;

import com.example.bookstore.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(
        name = "review_like",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_review_like_user_review", columnNames = {"user_id", "review_id"})
        },
        indexes = {
                @Index(name = "idx_review_like_user_id", columnList = "user_id"),
                @Index(name = "idx_review_like_review_id", columnList = "review_id")
        }
)
public class ReviewLike {

    @EmbeddedId
    private ReviewLikeId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("reviewId")
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;
}

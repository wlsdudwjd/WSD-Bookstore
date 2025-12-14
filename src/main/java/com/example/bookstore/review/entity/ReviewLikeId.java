package com.example.bookstore.review.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class ReviewLikeId implements Serializable {

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "review_id")
    private Integer reviewId;

    public ReviewLikeId(Integer userId, Integer reviewId) {
        this.userId = userId;
        this.reviewId = reviewId;
    }
}

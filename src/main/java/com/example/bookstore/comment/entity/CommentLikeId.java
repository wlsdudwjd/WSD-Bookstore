package com.example.bookstore.comment.entity;

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
public class CommentLikeId implements Serializable {

    @Column(name = "user_id", columnDefinition = "INT")
    private Integer userId;

    @Column(name = "comment_id", columnDefinition = "INT")
    private Integer commentId;

    public CommentLikeId(Integer userId, Integer commentId) {
        this.userId = userId;
        this.commentId = commentId;
    }
}

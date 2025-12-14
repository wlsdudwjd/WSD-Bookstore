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

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "comment_id")
    private Long commentId;

    public CommentLikeId(Integer userId, Long commentId) {
        this.userId = userId;
        this.commentId = commentId;
    }
}

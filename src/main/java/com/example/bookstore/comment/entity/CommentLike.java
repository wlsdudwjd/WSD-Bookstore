package com.example.bookstore.comment.entity;

import com.example.bookstore.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(
        name = "comment_like",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_comment_like_user_comment", columnNames = {"user_id", "comment_id"})
        },
        indexes = {
                @Index(name = "idx_comment_like_user_id", columnList = "user_id"),
                @Index(name = "idx_comment_like_comment_id", columnList = "comment_id")
        }
)
public class CommentLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;
}

package com.example.bookstore.wishlist.entity;

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
        name = "wishlist",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_wishlist_user_book", columnNames = {"user_id", "book_id"})
        },
        indexes = {
                @Index(name = "idx_wishlist_user_id", columnList = "user_id"),
                @Index(name = "idx_wishlist_book_id", columnList = "book_id")
        }
)
public class Wishlist {

    @EmbeddedId
    private WishlistId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("bookId")
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = DateUtil.now();
    }
}

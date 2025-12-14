package com.example.bookstore.wishlist.entity;

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
public class WishlistId implements Serializable {

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "book_id")
    private Long bookId;

    public WishlistId(Integer userId, Long bookId) {
        this.userId = userId;
        this.bookId = bookId;
    }
}

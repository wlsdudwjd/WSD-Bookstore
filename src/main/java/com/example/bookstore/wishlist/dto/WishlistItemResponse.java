package com.example.bookstore.wishlist.dto;

import com.example.bookstore.wishlist.entity.Wishlist;

import java.time.LocalDateTime;

public record WishlistItemResponse(
        Long favoriteId,
        Long bookId,
        String title,
        Integer price,
        LocalDateTime createdAt
) {
    public static WishlistItemResponse from(Wishlist wishlist) {
        return new WishlistItemResponse(
            wishlist.getFavoriteId(),
            wishlist.getBook().getBookId(),
            wishlist.getBook().getTitle(),
            wishlist.getBook().getPrice(),
            wishlist.getCreatedAt()
        );
    }
}

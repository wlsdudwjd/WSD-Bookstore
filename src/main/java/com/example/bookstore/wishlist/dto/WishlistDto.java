package com.example.bookstore.wishlist.dto;

import java.util.List;

import com.example.bookstore.wishlist.entity.Wishlist;

public record WishlistDto(Long userId, List<Long> bookIds) {
    public static WishlistDto from(Wishlist wishlist) {
        return new WishlistDto(wishlist.userId(), wishlist.bookIds());
    }
}

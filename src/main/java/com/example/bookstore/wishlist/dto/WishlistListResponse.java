package com.example.bookstore.wishlist.dto;

import java.util.List;

public record WishlistListResponse(
        List<WishlistItemResponse> items
) {
    public static WishlistListResponse of(List<WishlistItemResponse> items) {
        return new WishlistListResponse(items);
    }
}

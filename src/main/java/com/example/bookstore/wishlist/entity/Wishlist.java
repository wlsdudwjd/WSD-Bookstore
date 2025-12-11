package com.example.bookstore.wishlist.entity;

import java.util.List;

public record Wishlist(Long userId, List<Long> bookIds) {
}

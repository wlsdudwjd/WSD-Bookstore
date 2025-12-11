package com.example.bookstore.discount.entity;

import java.time.LocalDate;

public record Discount(Long id, String name, double percentage, LocalDate expiresAt) {
}

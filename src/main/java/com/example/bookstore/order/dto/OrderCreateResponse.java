package com.example.bookstore.order.dto;

import java.time.LocalDateTime;

public record OrderCreateResponse(
        Long orderId,
        LocalDateTime createdAt,
        String status
) {}
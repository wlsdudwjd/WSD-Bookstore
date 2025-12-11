package com.example.bookstore.order.dto;

import com.example.bookstore.order.entity.Order;
import com.example.bookstore.order.entity.OrderItem;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long orderId,
        String status,
        Integer totalAmount,
        LocalDateTime createdAt,
        List<OrderLine> items
) {
    public static OrderResponse from(Order order) {
        List<OrderLine> lines = order.getItems().stream()
                .map(OrderLine::from)
                .toList();
        return new OrderResponse(
                order.getOrderId(),
                order.getStatus().name().toLowerCase(),
                order.getTotalAmount(),
                order.getCreatedAt(),
                lines
        );
    }

    public record OrderLine(
            Long bookId,
            String title,
            Integer unitPrice,
            Integer quantity,
            Integer subtotal
    ) {
        public static OrderLine from(OrderItem item) {
            return new OrderLine(
                    item.getBook().getBookId(),
                    item.getBook().getTitle(),
                    item.getUnitPrice(),
                    item.getQuantity(),
                    item.getSubtotal()
            );
        }
    }
}

package com.example.bookstore.settlement.entity;

import java.util.List;

public record Settlement(Long id, double totalAmount, List<SettlementItem> items) {
}

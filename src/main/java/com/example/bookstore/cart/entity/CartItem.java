package com.example.bookstore.cart.entity;

import com.example.bookstore.book.entity.Book;
import com.example.bookstore.common.util.DateUtil;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(
        name = "cart_item",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_cart_item_cart_book",
                        columnNames = {"cart_id", "book_id"}
                )
        },
        indexes = {
                @Index(name = "idx_cart_item_cart_id", columnList = "cart_id"),
                @Index(name = "idx_cart_item_book_id", columnList = "book_id")
        }
)
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartItemId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Integer unitPrice;

    @Column(nullable = false)
    private Integer subtotal;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = DateUtil.now();
        this.updatedAt = DateUtil.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = DateUtil.now();
    }

    // ======================
    // 비즈니스 로직
    // ======================
    public void changeQuantity(int quantity) {
        this.quantity = quantity;
        this.subtotal = this.unitPrice * this.quantity;
    }

    // 연관관계 세터 (Cart에서만 사용)
    public void setCart(Cart cart) {
        this.cart = cart;
    }
}
package com.example.bookstore.order.entity;

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
        name = "order_items",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_order_items_order_book",
                        columnNames = {"order_id", "book_id"}
                )
        }
)
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;

    // 어떤 주문에 속해있는지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // 어떤 책인지
    @ManyToOne(fetch = FetchType.LAZY)
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

    // ================================
    // 생명주기
    // ================================
    @PrePersist
    public void onCreate() {
        this.createdAt = DateUtil.now();
        this.updatedAt = DateUtil.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = DateUtil.now();
    }

    // ================================
    // 정적 팩토리 메서드
    // ================================
    public static OrderItem create(Book book, int quantity, int unitPrice) {
        OrderItem item = new OrderItem();
        item.book = book;
        item.quantity = quantity;
        item.unitPrice = unitPrice;
        item.subtotal = unitPrice * quantity;
        return item;
    }

    // Order에서 양방향 연관관계 설정할 때 사용
    public void setOrder(Order order) {
        this.order = order;
    }
}
package com.example.bookstore.order.entity;

import com.example.bookstore.common.util.DateUtil;
import com.example.bookstore.order.entity.OrderItem;
import com.example.bookstore.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", columnDefinition = "INT")
    private Long orderId;

    // 주문자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, columnDefinition = "INT")
    private User user;

    // 총 금액
    @Column(name = "total_amount", nullable = false)
    private Integer totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // 주문 항목들
    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    // ================================
    // 생명주기
    // ================================
    @PrePersist
    public void onCreate() {
        this.createdAt = DateUtil.now();
        this.updatedAt = DateUtil.now();
        if (this.status == null) {
            this.status = OrderStatus.CREATED;
        }
        if (this.totalAmount == null) {
            this.totalAmount = 0;
        }
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = DateUtil.now();
    }

    // ================================
    // 정적 팩토리 메서드
    // ================================
    public static Order create(User user) {
        Order order = new Order();
        order.user = user;
        order.status = OrderStatus.CREATED;
        order.totalAmount = 0;
        order.items = new ArrayList<>();
        return order;
    }

    // ================================
    // 비즈니스 로직
    // ================================
    public void addItem(OrderItem item) {
        if (this.items == null) {
            this.items = new ArrayList<>();
        }
        this.items.add(item);
        item.setOrder(this); // 양방향 연관관계 설정
        if (this.totalAmount == null) {
            this.totalAmount = 0;
        }
        this.totalAmount += item.getSubtotal();
    }

    public void changeStatus(OrderStatus status) {
        this.status = status;
    }
}

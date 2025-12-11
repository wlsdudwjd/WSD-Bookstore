package com.example.bookstore.seller.entity;

import com.example.bookstore.common.util.DateUtil;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "seller")
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sellerId;

    @Column(nullable = false, length = 255)
    private String businessName;

    @Column(nullable = false, length = 255)
    private String address;

    @Column(nullable = false, length = 20)
    private String phoneNumber;

    @Column(nullable = false, length = 255)
    private String email;

    @Column(nullable = false, length = 50)
    private String businessNumber;

    @Column(nullable = false, length = 100)
    private String payoutBank;

    @Column(nullable = false, length = 100)
    private String payoutAccount;

    @Column(nullable = false, length = 100)
    private String payoutHolder;

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
}
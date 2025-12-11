package com.example.bookstore.seller.entity;

import com.example.bookstore.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "seller")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Seller extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seller_id")
    private Long id;

    @Column(name = "business_name", nullable = false, length = 255)
    private String businessName;

    @Column(name = "business_number", nullable = false, length = 50)
    private String businessNumber;

    @Column(nullable = false, length = 255)
    private String email;

    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    @Column(nullable = false, length = 255)
    private String address;

    @Column(name = "payout_bank", nullable = false, length = 100)
    private String payoutBank;

    @Column(name = "payout_account", nullable = false, length = 100)
    private String payoutAccount;

    @Column(name = "payout_holder", nullable = false, length = 100)
    private String payoutHolder;
}
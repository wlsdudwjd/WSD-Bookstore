package com.example.bookstore.discount.service;

import com.example.bookstore.discount.entity.Discount;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class DiscountService {

    public List<Discount> getDiscounts() {
        // 아직 할인 데이터 저장소가 없으므로 기본값 반환
        return List.of();
    }

    public Discount getDiscount(Long discountId) {
        // 기본 더미 데이터
        return new Discount(discountId, "sample", 0.0, LocalDate.now());
    }
}

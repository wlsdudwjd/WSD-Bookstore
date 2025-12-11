package com.example.bookstore.settlement.service;

import com.example.bookstore.settlement.entity.Settlement;
import com.example.bookstore.settlement.entity.SettlementItem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@Transactional(readOnly = true)
public class SettlementService {

    public Settlement getLatestSettlement(Long sellerId) {
        // 실제 정산 로직이 없으므로 빈 정산 정보 반환
        return new Settlement(sellerId, 0.0, Collections.<SettlementItem>emptyList());
    }
}

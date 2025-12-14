package com.example.bookstore.settlement.controller;

import com.example.bookstore.common.api.ApiResponse;
import com.example.bookstore.settlement.entity.Settlement;
import com.example.bookstore.settlement.service.SettlementService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/settlements")
@RequiredArgsConstructor
@Tag(name = "Settlement", description = "정산 API")
public class SettlementController {

    private final SettlementService settlementService;

    @GetMapping("/sellers/{sellerId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Settlement> getSettlement(@PathVariable Long sellerId) {
        return ApiResponse.success("조회 성공", settlementService.getLatestSettlement(sellerId));
    }
}

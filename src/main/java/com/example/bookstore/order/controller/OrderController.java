package com.example.bookstore.order.controller;

import com.example.bookstore.common.api.ApiResponse;
import com.example.bookstore.order.dto.OrderCreateRequest;
import com.example.bookstore.order.dto.OrderCreateResponse;
import com.example.bookstore.order.dto.OrderResponse;
import com.example.bookstore.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Order", description = "주문 API")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "주문 생성", description = "장바구니/도서 목록을 기반으로 주문을 생성합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "주문 생성 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "도서 또는 사용자 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public ApiResponse<OrderCreateResponse> createOrder(@RequestBody @Valid OrderCreateRequest request) {
        OrderCreateResponse response = orderService.createOrder(request);
        return ApiResponse.success("주문이 정상적으로 생성되었습니다.", response);
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "주문 상세 조회", description = "주문의 상태, 총액, 항목을 조회합니다.")
    public ApiResponse<OrderResponse> getOrder(@PathVariable Long orderId) {
        OrderResponse response = orderService.getOrder(orderId);
        return ApiResponse.success("조회 성공", response);
    }
}

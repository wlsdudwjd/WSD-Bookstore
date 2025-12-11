package com.example.bookstore.order.service;

import com.example.bookstore.book.entity.Book;
import com.example.bookstore.book.repository.BookRepository;
import com.example.bookstore.common.exception.CustomException;
import com.example.bookstore.common.exception.ErrorCode;
import com.example.bookstore.common.util.SecurityUtil;
import com.example.bookstore.order.dto.OrderCreateRequest;
import com.example.bookstore.order.dto.OrderCreateResponse;
import com.example.bookstore.order.dto.OrderItemRequest;
import com.example.bookstore.order.entity.Order;
import com.example.bookstore.order.entity.OrderItem;
import com.example.bookstore.order.repository.OrderRepository;
import com.example.bookstore.user.entity.User;
import com.example.bookstore.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Transactional
    public OrderCreateResponse createOrder(OrderCreateRequest request) {

        if (request.items() == null || request.items().isEmpty()) {
            throw new CustomException(
                    ErrorCode.INVALID_REQUEST,
                    Map.of("items", "주문 항목이 비어 있습니다.")
            );
        }

        Long userId = SecurityUtil.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(
                        ErrorCode.USER_NOT_FOUND,
                        Map.of("userId", "사용자를 찾을 수 없습니다.")
                ));

        Order order = Order.create(user);

        for (OrderItemRequest itemReq : request.items()) {
            Book book = bookRepository.findById(itemReq.bookId())
                    .orElseThrow(() -> new CustomException(
                            ErrorCode.RESOURCE_NOT_FOUND,
                            Map.of("bookId", "도서를 찾을 수 없습니다.")
                    ));

            int unitPrice = book.getPrice();
            OrderItem item = OrderItem.create(book, itemReq.quantity(), unitPrice);
            order.addItem(item);
        }

        Order saved = orderRepository.save(order);

        return new OrderCreateResponse(
                saved.getOrderId(),
                saved.getCreatedAt(),
                saved.getStatus().name().toLowerCase() // created / paid ...
        );
    }
}
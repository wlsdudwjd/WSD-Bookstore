package com.example.bookstore.cart.service;

import com.example.bookstore.book.entity.Book;
import com.example.bookstore.book.repository.BookRepository;
import com.example.bookstore.cart.dto.CartItemRequest;
import com.example.bookstore.cart.dto.CartItemResponse;
import com.example.bookstore.cart.dto.CartResponse;
import com.example.bookstore.cart.entity.Cart;
import com.example.bookstore.cart.entity.CartItem;
import com.example.bookstore.cart.repository.CartItemRepository;
import com.example.bookstore.cart.repository.CartRepository;
import com.example.bookstore.common.exception.CustomException;
import com.example.bookstore.common.exception.ErrorCode;
import com.example.bookstore.user.entity.User;
import com.example.bookstore.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    // 유저의 장바구니 조회
    public CartResponse getMyCart(Long userId) {
        Cart cart = getOrCreateCart(userId);
        List<CartItemResponse> items = cart.getItems()
                .stream()
                .map(CartItemResponse::from)
                .toList();
        return CartResponse.of(items);
    }

    // 아이템 추가/증가
    @Transactional
    public CartResponse addItem(Long userId, CartItemRequest request) {
        Cart cart = getOrCreateCart(userId);

        Book book = bookRepository.findById(request.bookId())
                .orElseThrow(() -> new CustomException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        Map.of("bookId", "도서를 찾을 수 없습니다.")
                ));

        CartItem item = cartItemRepository.findByCartAndBook(cart, book)
                .orElseGet(() -> {
                    CartItem newItem = CartItem.builder()
                            .cart(cart)
                            .book(book)
                            .quantity(0)
                            .unitPrice(book.getPrice())
                            .subtotal(0)
                            .build();
                    cart.addItem(newItem);
                    return newItem;
                });

        int newQuantity = item.getQuantity() + request.quantity();
        item.changeQuantity(newQuantity);

        cartItemRepository.save(item);

        List<CartItemResponse> items = cart.getItems()
                .stream()
                .map(CartItemResponse::from)
                .toList();
        return CartResponse.of(items);
    }

    // 수량 변경 (절대값으로 세팅)
    @Transactional
    public CartResponse updateItemQuantity(Long userId, Long cartItemId, int quantity) {
        if (quantity <= 0) {
            // 0 이하이면 그냥 삭제
            removeItem(userId, cartItemId);
            return getMyCart(userId);
        }

        Cart cart = getOrCreateCart(userId);

        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CustomException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        Map.of("cartItemId", "장바구니 아이템을 찾을 수 없습니다.")
                ));

        // 내 카트 아이템인지 검증
        if (!item.getCart().getCartId().equals(cart.getCartId())) {
            throw new CustomException(
                    ErrorCode.FORBIDDEN,
                    Map.of("cartItemId", "해당 장바구니 아이템에 접근할 수 없습니다.")
            );
        }

        item.changeQuantity(quantity);

        List<CartItemResponse> items = cart.getItems()
                .stream()
                .map(CartItemResponse::from)
                .toList();
        return CartResponse.of(items);
    }

    // 아이템 삭제
    @Transactional
    public void removeItem(Long userId, Long cartItemId) {
        Cart cart = getOrCreateCart(userId);

        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CustomException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        Map.of("cartItemId", "장바구니 아이템을 찾을 수 없습니다.")
                ));

        if (!item.getCart().getCartId().equals(cart.getCartId())) {
            throw new CustomException(
                    ErrorCode.FORBIDDEN,
                    Map.of("cartItemId", "해당 장바구니 아이템에 접근할 수 없습니다.")
            );
        }

        cart.removeItem(item);
        cartItemRepository.delete(item);
    }

    // 전체 비우기
    @Transactional
    public void clearCart(Long userId) {
        Cart cart = getOrCreateCart(userId);
        cart.getItems().clear();
    }

    // ======================
    // 내부 헬퍼
    // ======================
    @Transactional
    protected Cart getOrCreateCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(
                        ErrorCode.USER_NOT_FOUND,
                        Map.of("userId", "사용자를 찾을 수 없습니다.")
                ));

        return cartRepository.findByUser(user)
                .orElseGet(() -> cartRepository.save(
                        Cart.builder()
                                .user(user)
                                .build()
                ));
    }
}
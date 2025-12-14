package com.example.bookstore.cart.service;

import com.example.bookstore.book.entity.Book;
import com.example.bookstore.book.repository.BookRepository;
import com.example.bookstore.cart.dto.CartItemRequest;
import com.example.bookstore.cart.dto.CartItemResponse;
import com.example.bookstore.cart.dto.CartItemUpsertResponse;
import com.example.bookstore.cart.dto.CartResponse;
import com.example.bookstore.cart.entity.Cart;
import com.example.bookstore.cart.entity.CartItem;
import com.example.bookstore.cart.repository.CartItemRepository;
import com.example.bookstore.cart.repository.CartRepository;
import com.example.bookstore.common.exception.CustomException;
import com.example.bookstore.common.exception.ErrorCode;
import com.example.bookstore.common.util.DateUtil;
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
    public CartResponse getMyCart(Integer userId) {
        Cart cart = getOrCreateCart(userId);
        List<CartItemResponse> items = cart.getItems()
                .stream()
                .map(CartItemResponse::from)
                .toList();
        return CartResponse.of(items);
    }

    // 아이템 upsert (절대 수량 지정, 0이면 삭제)
    @Transactional
    public CartItemUpsertResponse upsertItem(Integer userId, CartItemRequest request) {
        Cart cart = getOrCreateCart(userId);

        Book book = bookRepository.findById(request.bookId())
                .orElseThrow(() -> new CustomException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        Map.of("bookId", "도서를 찾을 수 없습니다.")
                ));

        CartItem item = cartItemRepository.findByCartAndBook(cart, book).orElse(null);

        if (request.quantity() <= 0) {
            if (item != null) {
                cart.removeItem(item);
                cartItemRepository.delete(item);
            }
            return new CartItemUpsertResponse(null, 0, DateUtil.now());
        }

        if (item == null) {
            item = CartItem.builder()
                    .cart(cart)
                    .book(book)
                    .quantity(request.quantity())
                    .unitPrice(book.getPrice())
                    .subtotal(book.getPrice() * request.quantity())
                    .build();
            cart.addItem(item);
        } else {
            item.changeQuantity(request.quantity());
        }

        cartItemRepository.save(item);

        return new CartItemUpsertResponse(
                item.getCartItemId(),
                item.getQuantity(),
                DateUtil.now()
        );
    }

    // 수량 변경 (절대값으로 세팅)
    @Transactional
    public CartResponse updateItemQuantity(Integer userId, Long cartItemId, int quantity) {
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
    public void removeItem(Integer userId, Long cartItemId) {
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
    public void clearCart(Integer userId) {
        Cart cart = getOrCreateCart(userId);
        cart.getItems().clear();
    }

    // ======================
    // 내부 헬퍼
    // ======================
    @Transactional
    protected Cart getOrCreateCart(Integer userId) {
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

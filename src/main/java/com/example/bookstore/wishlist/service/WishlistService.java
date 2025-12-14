package com.example.bookstore.wishlist.service;

import com.example.bookstore.book.entity.Book;
import com.example.bookstore.book.repository.BookRepository;
import com.example.bookstore.common.exception.CustomException;
import com.example.bookstore.common.exception.ErrorCode;
import com.example.bookstore.user.entity.User;
import com.example.bookstore.user.repository.UserRepository;
import com.example.bookstore.wishlist.dto.WishlistAddResponse;
import com.example.bookstore.wishlist.dto.WishlistItemResponse;
import com.example.bookstore.wishlist.dto.WishlistListResponse;
import com.example.bookstore.wishlist.dto.WishlistRequest;
import com.example.bookstore.wishlist.entity.Wishlist;
import com.example.bookstore.wishlist.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Transactional
    public WishlistAddResponse add(Integer userId, WishlistRequest request) {
        User user = getUser(userId);
        Book book = bookRepository.findById(request.bookId())
                .orElseThrow(() -> new CustomException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        Map.of("bookId", "도서를 찾을 수 없습니다.")
                ));

        Wishlist existing = wishlistRepository.findByUserAndBook(user, book).orElse(null);
        if (existing != null) {
            return new WishlistAddResponse(existing.getFavoriteId(), existing.getCreatedAt());
        }

        Wishlist wishlist = Wishlist.builder()
                .user(user)
                .book(book)
                .build();

        Wishlist saved = wishlistRepository.save(wishlist);
        return new WishlistAddResponse(saved.getFavoriteId(), saved.getCreatedAt());
    }

    public WishlistListResponse getList(Integer userId) {
        User user = getUser(userId);
        List<WishlistItemResponse> items = wishlistRepository.findAllByUser(user).stream()
                .map(WishlistItemResponse::from)
                .toList();
        return WishlistListResponse.of(items);
    }

    @Transactional
    public void remove(Integer userId, Long favoriteId) {
        User user = getUser(userId);
        Wishlist wishlist = wishlistRepository.findById(favoriteId)
                .orElseThrow(() -> new CustomException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        Map.of("favoriteId", "위시리스트 항목을 찾을 수 없습니다.")
                ));

        if (!wishlist.getUser().getUserId().equals(user.getUserId())) {
            throw new CustomException(
                    ErrorCode.FORBIDDEN,
                    Map.of("favoriteId", "해당 위시리스트 항목에 접근할 수 없습니다.")
            );
        }

        wishlistRepository.delete(wishlist);
    }

    private User getUser(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(
                        ErrorCode.USER_NOT_FOUND,
                        Map.of("userId", "사용자를 찾을 수 없습니다.")
                ));
    }
}

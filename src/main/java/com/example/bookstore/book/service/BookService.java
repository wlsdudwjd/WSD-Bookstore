package com.example.bookstore.book.service;

import com.example.bookstore.book.dto.BookCreateRequest;
import com.example.bookstore.book.dto.BookResponse;
import com.example.bookstore.book.dto.BookSearchCondition;
import com.example.bookstore.book.dto.BookUpdateRequest;
import com.example.bookstore.book.entity.Book;
import com.example.bookstore.book.repository.BookRepository;
import com.example.bookstore.common.exception.CustomException;
import com.example.bookstore.common.exception.ErrorCode;
import com.example.bookstore.seller.entity.Seller;
import com.example.bookstore.seller.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;
    private final SellerRepository sellerRepository;

    @Transactional
    public BookResponse createBook(BookCreateRequest request) {

        if (bookRepository.existsByIsbn(request.isbn())) {
            throw new CustomException(
                    ErrorCode.DUPLICATE_RESOURCE,
                    Map.of("isbn", "이미 사용 중인 ISBN 입니다.")
            );
        }

        Seller seller = sellerRepository.findById(request.sellerId())
                .orElseThrow(() -> new CustomException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        Map.of("sellerId", "판매자를 찾을 수 없습니다.")
                ));

        Book book = Book.builder()
                .seller(seller)
                .title(request.title())
                .author(request.author())
                .publisher(request.publisher())
                .isbn(request.isbn())
                .price(request.price())
                .publicationDate(request.publicationDate())
                .summary(request.summary())
                .build();

        Book saved = bookRepository.save(book);
        return BookResponse.from(saved);
    }

    public BookResponse getBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new CustomException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        Map.of("bookId", "도서를 찾을 수 없습니다.")
                ));
        return BookResponse.from(book);
    }

    public Page<BookResponse> getBooks(BookSearchCondition cond, Pageable pageable) {
        Page<Book> page = bookRepository.searchBooks(
                cond.keyword(),
                cond.publisher(),
                cond.minPrice(),
                cond.maxPrice(),
                cond.dateFrom(),
                cond.dateTo(),
                pageable
        );
        return page.map(BookResponse::from);
    }

    @Transactional
    public BookResponse updateBook(Long bookId, BookUpdateRequest request) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new CustomException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        Map.of("bookId", "도서를 찾을 수 없습니다.")
                ));

        book.update(
                request.title(),
                request.author(),
                request.publisher(),
                request.price(),
                request.publicationDate(),
                request.summary()
        );

        return BookResponse.from(book);
    }

    @Transactional
    public void deleteBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new CustomException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        Map.of("bookId", "도서를 찾을 수 없습니다.")
                ));

        bookRepository.delete(book);
    }

    public Page<BookResponse> getBooksBySeller(Long sellerId, Pageable pageable) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new CustomException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        Map.of("sellerId", "판매자를 찾을 수 없습니다.")
                ));

        Page<Book> page = bookRepository.findBySeller(seller, pageable);
        return page.map(BookResponse::from);
    }
}
package com.example.bookstore.book.dto;

import com.example.bookstore.book.entity.Book;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record BookResponse(
        Long bookId,
        String title,
        String author,
        String publisher,
        String isbn,
        Integer price,
        LocalDate publicationDate,
        String summary,
        Long sellerId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static BookResponse from(Book book) {
        return new BookResponse(
                book.getBookId(),
                book.getTitle(),
                book.getAuthor(),
                book.getPublisher(),
                book.getIsbn(),
                book.getPrice(),
                book.getPublicationDate(),
                book.getSummary(),
                book.getSeller().getSellerId(),
                book.getCreatedAt(),
                book.getUpdatedAt()
        );
    }
}
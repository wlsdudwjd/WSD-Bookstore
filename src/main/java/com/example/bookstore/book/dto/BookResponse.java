package com.example.bookstore.book.dto;

import com.example.bookstore.book.entity.Book;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@JsonInclude(Include.NON_NULL)
public record BookResponse(
        Long bookId,
        String title,
        String publisher,
        Integer price,
        String isbn,
        LocalDate publicationDate,
        String summary,
        List<String> authors,
        List<CategoryItem> categories,
        Double ratingAvg,
        Integer likeCount,
        RankInfo rankInfo,
        Statistic statistic,
        Long sellerId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static BookResponse from(
            Book book,
            Double ratingAvg,
            Integer likeCount
    ) {
        return new BookResponse(
                book.getBookId(),
                book.getTitle(),
                book.getPublisher(),
                book.getPrice(),
                book.getIsbn(),
                book.getPublicationDate(),
                book.getSummary(),
                book.getAuthor() != null ? List.of(book.getAuthor()) : Collections.emptyList(),
                Collections.emptyList(),
                ratingAvg,
                likeCount,
                null,
                null,
                book.getSeller().getSellerId(),
                book.getCreatedAt(),
                book.getUpdatedAt()
        );
    }

    public record CategoryItem(Long categoryId, String name) {}

    public record RankInfo(Integer rank, Integer purchaseCount) {}

    public record Statistic(Integer favoritedThenPurchasedUserCount, Integer viewedThenPurchasedUserCount) {}
}

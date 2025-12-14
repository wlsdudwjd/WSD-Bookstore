package com.example.bookstore.book.entity;

import com.example.bookstore.common.util.DateUtil;
import com.example.bookstore.seller.entity.Seller;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(
        name = "book",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_book_isbn", columnNames = "isbn")
        },
        indexes = {
                @Index(name = "idx_book_seller_id", columnList = "seller_id"),
                @Index(name = "idx_book_isbn", columnList = "isbn")
        }
)
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id", columnDefinition = "INT")
    private Long bookId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "seller_id", nullable = false, columnDefinition = "INT")
    private Seller seller;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, length = 255)
    private String author;

    @Column(nullable = false, length = 255)
    private String publisher;

    @Column(nullable = false, length = 20)
    private String isbn;

    @Column(nullable = false)
    private Integer price;

    @Column(name = "publication_date", nullable = false)
    private LocalDate publicationDate;

    @Lob
    private String summary;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = DateUtil.now();
        this.updatedAt = DateUtil.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = DateUtil.now();
    }

    public void update(
            String title,
            String author,
            String publisher,
            Integer price,
            LocalDate publicationDate,
            String summary
    ) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.price = price;
        this.publicationDate = publicationDate;
        this.summary = summary;
    }
}

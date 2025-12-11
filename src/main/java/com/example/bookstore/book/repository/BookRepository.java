package com.example.bookstore.book.repository;

import com.example.bookstore.book.entity.Book;
import com.example.bookstore.seller.entity.Seller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long> {

    boolean existsByIsbn(String isbn);

    Page<Book> findBySeller(Seller seller, Pageable pageable);

    @Query("""
            select b
            from Book b
            where (:keyword is null
                   or lower(b.title) like lower(concat('%', :keyword, '%'))
                   or lower(b.author) like lower(concat('%', :keyword, '%'))
                   or b.isbn like concat('%', :keyword, '%'))
              and (:publisher is null or b.publisher = :publisher)
              and (:minPrice is null or b.price >= :minPrice)
              and (:maxPrice is null or b.price <= :maxPrice)
              and (:dateFrom is null or b.publicationDate >= :dateFrom)
              and (:dateTo is null or b.publicationDate <= :dateTo)
            """)
    Page<Book> searchBooks(
            @Param("keyword") String keyword,
            @Param("publisher") String publisher,
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice,
            @Param("dateFrom") java.time.LocalDate dateFrom,
            @Param("dateTo") java.time.LocalDate dateTo,
            Pageable pageable
    );
}
package com.example.bookstore.ranking.service;

import com.example.bookstore.book.entity.Book;
import com.example.bookstore.book.repository.BookRepository;
import com.example.bookstore.ranking.entity.Ranking;
import com.example.bookstore.wishlist.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RankingService {

    private final BookRepository bookRepository;
    private final WishlistRepository wishlistRepository;

    /**
     * 간단한 랭킹: 위시리스트 찜 수 기준 상위 10권 반환
     */
    public List<Ranking> getTopBooks() {
        AtomicInteger position = new AtomicInteger(1);
        return bookRepository.findAll().stream()
                .sorted(Comparator.comparingLong(this::likeCountFor).reversed())
                .limit(10)
                .map(book -> new Ranking(position.getAndIncrement(), book.getBookId(), book.getTitle()))
                .toList();
    }

    private long likeCountFor(Book book) {
        return wishlistRepository.countByBook(book);
    }
}

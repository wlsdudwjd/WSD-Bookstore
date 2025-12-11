package com.example.bookstore;

import com.example.bookstore.book.entity.Book;
import com.example.bookstore.book.repository.BookRepository;
import com.example.bookstore.seller.entity.Seller;
import com.example.bookstore.seller.repository.SellerRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class TestDataInitializer {

    private final SellerRepository sellerRepository;
    private final BookRepository bookRepository;

    @PostConstruct
    public void init() {
        // 이미 데이터 있으면 스킵 (서버 재시작 고려)
        if (bookRepository.count() > 0) {
            return;
        }

        // 1) 판매자 하나 생성
        Seller seller = Seller.builder()
                .businessName("테스트 서점")
                .businessNumber("123-45-67890")
                .email("seller1@example.com")
                .phoneNumber("010-9999-9999")
                .payoutBank("국민은행")
                .payoutAccount("1234567890")
                .payoutHolder("테스트 서점")
                .address("서울시 어딘가 1-1")
                .build();

        sellerRepository.save(seller);

        // 2) 책 하나 생성
        Book book = Book.builder()
                .isbn("9781234567890")
                .title("테스트 도서")
                .author("테스트 작가")
                .publisher("테스트 출판사")
                .publicationDate(LocalDate.of(2024, 1, 1))
                .price(15000)
                .summary("장바구니 테스트용 도서입니다.")
                .seller(seller)
                .build();

        bookRepository.save(book);
    }
}
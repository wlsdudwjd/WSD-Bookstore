package com.example.bookstore.book.controller;

import com.example.bookstore.book.dto.*;
import com.example.bookstore.book.service.BookService;
import com.example.bookstore.common.api.ApiResponse;
import com.example.bookstore.common.api.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
@Tag(name = "Book", description = "도서 관련 API")
public class BookController {

    private final BookService bookService;

    // ============================
    // 도서 등록 (ADMIN)
    // ============================
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "도서 등록 (ADMIN)", description = "새로운 도서를 등록합니다.")
    public ApiResponse<BookResponse> createBook(
            @RequestBody @Valid BookCreateRequest request
    ) {
        return ApiResponse.created(bookService.createBook(request));
    }

    // ============================
    // 도서 단건 조회
    // ============================
    @GetMapping("/{bookId}")
    @Operation(summary = "도서 상세 조회", description = "도서 ID로 상세 정보를 조회합니다.")
    public ApiResponse<BookResponse> getBook(@PathVariable Long bookId) {
        return ApiResponse.ok(bookService.getBook(bookId));
    }

    // ============================
    // 도서 목록 조회 (검색/필터/페이지네이션)
    // ============================
    @GetMapping
    @Operation(
            summary = "도서 목록 조회",
            description = "검색어, 출판사, 가격범위, 출간일 범위로 필터링하여 도서 목록을 조회합니다."
    )
    public ApiResponse<PageResponse<BookResponse>> getBooks(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String publisher,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate dateFrom,
            @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate dateTo,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        BookSearchCondition cond = new BookSearchCondition(
                keyword,
                publisher,
                minPrice,
                maxPrice,
                dateFrom,
                dateTo
        );

        var page = bookService.getBooks(cond, pageable);
        return ApiResponse.ok(PageResponse.from(page));
    }

    // ============================
    // 도서 수정 (ADMIN)
    // ============================
    @PatchMapping("/{bookId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "도서 수정 (ADMIN)", description = "도서 정보를 수정합니다.")
    public ApiResponse<BookResponse> updateBook(
            @PathVariable Long bookId,
            @RequestBody @Valid BookUpdateRequest request
    ) {
        return ApiResponse.ok(bookService.updateBook(bookId, request));
    }

    // ============================
    // 도서 삭제 (ADMIN)
    // ============================
    @DeleteMapping("/{bookId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "도서 삭제 (ADMIN)", description = "도서를 삭제합니다.")
    public ApiResponse<Void> deleteBook(@PathVariable Long bookId) {
        bookService.deleteBook(bookId);
        return ApiResponse.noContent();
    }

    // ============================
    // 판매자별 도서 목록
    // ============================
    @GetMapping("/seller/{sellerId}")
    @Operation(summary = "판매자별 도서 목록", description = "특정 판매자가 등록한 도서 목록을 조회합니다.")
    public ApiResponse<PageResponse<BookResponse>> getBooksBySeller(
            @PathVariable Long sellerId,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        var page = bookService.getBooksBySeller(sellerId, pageable);
        return ApiResponse.ok(PageResponse.from(page));
    }
}
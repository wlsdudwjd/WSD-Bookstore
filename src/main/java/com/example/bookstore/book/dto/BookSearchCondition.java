package com.example.bookstore.book.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record BookSearchCondition(

        String keyword,

        String publisher,

        Integer minPrice,
        Integer maxPrice,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate dateFrom,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate dateTo
) {
}
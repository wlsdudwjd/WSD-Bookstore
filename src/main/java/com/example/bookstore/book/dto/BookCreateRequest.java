package com.example.bookstore.book.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record BookCreateRequest(
        @NotBlank String title,
        @NotBlank String author,
        @NotBlank String publisher,
        @NotBlank String isbn,
        @NotNull @Min(0) Integer price,
        @NotNull LocalDate publicationDate,
        @NotNull Long sellerId
) {
}
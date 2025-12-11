package com.example.bookstore.book.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record BookUpdateRequest(

        @NotBlank
        @Size(max = 255)
        String title,

        @NotBlank
        @Size(max = 255)
        String author,

        @NotBlank
        @Size(max = 255)
        String publisher,

        @NotNull
        @Positive
        Integer price,

        @NotNull
        LocalDate publicationDate,

        @Size(max = 4000)
        String summary
) {
}
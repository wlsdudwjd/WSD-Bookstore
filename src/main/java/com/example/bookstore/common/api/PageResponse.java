package com.example.bookstore.common.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T> {

    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private List<T> content;

    public static <T> PageResponse<T> from(
            org.springframework.data.domain.Page<T> pageData
    ) {
        return new PageResponse<>(
                pageData.getNumber(),
                pageData.getSize(),
                pageData.getTotalElements(),
                pageData.getTotalPages(),
                pageData.getContent()
        );
    }
}
package com.example.bookstore.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
        @NotBlank
        @Size(max = 100)
        String name,

        @NotBlank
        @Size(max = 20)
        String phoneNumber,

        @NotBlank
        @Size(max = 255)
        String address
) {
}
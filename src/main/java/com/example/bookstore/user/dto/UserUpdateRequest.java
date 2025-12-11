package com.example.bookstore.user.dto;

import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
        @Size(max = 100)
        String name,

        @Size(max = 20)
        String phoneNumber,

        @Size(max = 255)
        String address,

        @Size(min = 8, max = 255)
        String password
) {
}

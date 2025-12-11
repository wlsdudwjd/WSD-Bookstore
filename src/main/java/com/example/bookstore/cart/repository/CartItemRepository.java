package com.example.bookstore.cart.repository;

import com.example.bookstore.cart.entity.Cart;
import com.example.bookstore.cart.entity.CartItem;
import com.example.bookstore.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCartAndBook(Cart cart, Book book);
}
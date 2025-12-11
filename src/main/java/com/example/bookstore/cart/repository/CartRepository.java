package com.example.bookstore.cart.repository;

import com.example.bookstore.cart.entity.Cart;
import com.example.bookstore.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUser(User user);
}
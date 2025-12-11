package com.example.bookstore.wishlist.repository;

import com.example.bookstore.book.entity.Book;
import com.example.bookstore.user.entity.User;
import com.example.bookstore.wishlist.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    Optional<Wishlist> findByUserAndBook(User user, Book book);

    List<Wishlist> findAllByUser(User user);

    long countByBook(Book book);
}

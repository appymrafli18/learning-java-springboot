package com.ecommerce.repositories;

import com.ecommerce.entity.Cart;
import com.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("SELECT c FROM Cart c JOIN FETCH c.items ci JOIN FETCH ci.product WHERE c.user.id = :userId")
    Optional<Cart> findByUserId(Long userId);

    Long user(User user);
}

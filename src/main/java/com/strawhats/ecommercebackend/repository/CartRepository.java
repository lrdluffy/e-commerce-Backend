package com.strawhats.ecommercebackend.repository;

import com.strawhats.ecommercebackend.model.Cart;
import com.strawhats.ecommercebackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findCartByUser(User user);

    Optional<Cart> findCartByUserAndCartId(User user, Long cartId);
}

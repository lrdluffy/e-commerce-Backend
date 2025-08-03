package com.strawhats.ecommercebackend.repository;

import com.strawhats.ecommercebackend.model.Cart;
import com.strawhats.ecommercebackend.model.CartItem;
import com.strawhats.ecommercebackend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findCartItemByCartAndProduct(Cart cart, Product product);

    Optional<CartItem> findCartItemByCartItemId(Long cartItemId);

    @Query("SELECT cartItem FROM CartItem cartItem WHERE cartItem.cart = :cart AND cartItem.product.productId = :productId")
    Optional<CartItem> findCartItemByCartAndProductId(@Param("cart") Cart cart, @Param("productId") Long productId);
}

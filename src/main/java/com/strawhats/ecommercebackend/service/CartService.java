package com.strawhats.ecommercebackend.service;

import com.strawhats.ecommercebackend.model.Cart;
import com.strawhats.ecommercebackend.payload.CartDTO;
import com.strawhats.ecommercebackend.payload.CartItemDTO;

public interface CartService {

    CartDTO addToCart(Long userId, CartItemDTO cartItemDTO);

    CartDTO getCart(Long userId);

    CartDTO removeFromCart(Long userId, Long cartItemId);

    CartDTO clearCart(Long cartId);

    CartDTO updateCartItem(Long cartItemId, CartItemDTO cartItemDTO);

    Cart deleteCart(Long cartId);
}

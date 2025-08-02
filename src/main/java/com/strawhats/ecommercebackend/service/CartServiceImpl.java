package com.strawhats.ecommercebackend.service;

import com.strawhats.ecommercebackend.exception.ApiException;
import com.strawhats.ecommercebackend.exception.ResourceNotFoundException;
import com.strawhats.ecommercebackend.model.Cart;
import com.strawhats.ecommercebackend.model.CartItem;
import com.strawhats.ecommercebackend.model.Product;
import com.strawhats.ecommercebackend.model.User;
import com.strawhats.ecommercebackend.payload.CartDTO;
import com.strawhats.ecommercebackend.payload.CartItemDTO;
import com.strawhats.ecommercebackend.repository.CartItemRepository;
import com.strawhats.ecommercebackend.repository.CartRepository;
import com.strawhats.ecommercebackend.repository.ProductRepository;
import com.strawhats.ecommercebackend.utils.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CartServiceImpl implements CartService {

    private final AuthUtils authUtils;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final ModelMapper modelMapper;
    private final CartRepository cartRepository;

    @Override
    public CartDTO addToCart(Long userId, CartItemDTO cartItemDTO) {
        User user = authUtils.loggedInUser();
        Cart userCart = cartRepository.findCartByUser(user)
                .orElseGet(Cart::new);

        Product product = productRepository.findProductByProductId(cartItemDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", cartItemDTO.getProductId()));

        if (cartItemDTO.getQuantity() > product.getStock()) {
            throw new ApiException("Requested quantity is greater than stock");
        }

        cartItemRepository.findCartItemByCartAndProduct(userCart, product)
                .ifPresent(cartItem -> {
                    throw new ApiException("There is already a product with the same product id in your cart");
                });

        CartItem cartItem = modelMapper.map(cartItemDTO, CartItem.class);
        cartItem.setCart(userCart);
        cartItem.setProduct(product);
        cartItem.setPrice(product.getPrice());
        cartItemRepository.save(cartItem);

        userCart.setTotalPrice(user.getCart().getTotalPrice().add(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()))));
        Cart savedCart = cartRepository.save(userCart);

        List<CartItemDTO> cartItemDTOs = savedCart.getCartItems().stream()
                .map(item -> {
                    CartItemDTO itemDTO = new CartItemDTO();
                    itemDTO.setProductId(item.getProduct().getProductId());
                    itemDTO.setQuantity(item.getQuantity());
                    return itemDTO;
                }).toList();

        CartDTO cartDTO = new CartDTO();
        cartDTO.setItems(cartItemDTOs);
        cartDTO.setTotalPrice(savedCart.getTotalPrice());

        return cartDTO;
    }

    @Override
    public CartDTO getCart(Long userId) {
        User user = authUtils.loggedInUser();
        if (!user.getUserId().equals(userId)) {
            throw new ApiException("Couldn't fetch cart details");
        }

        Cart cart = cartRepository.findCartByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "userId", userId));


        List<CartItemDTO> cartItemDTOs = cart.getCartItems().stream()
                .map(item -> {
                    CartItemDTO itemDTO = new CartItemDTO();
                    itemDTO.setProductId(item.getProduct().getProductId());
                    itemDTO.setQuantity(item.getQuantity());
                    return itemDTO;
                }).toList();

        CartDTO cartDTO = new CartDTO();
        cartDTO.setItems(cartItemDTOs);
        cartDTO.setTotalPrice(cart.getTotalPrice());

        return cartDTO;
    }

    @Override
    public CartDTO removeFromCart(Long userId, Long cartItemId) {
        User user = authUtils.loggedInUser();
        if (!user.getUserId().equals(userId)) {
            throw new ApiException("Couldn't fetch cart details");
        }

        Cart cart = cartRepository.findCartByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "userId", userId));
        CartItem cartItem = cartItemRepository.findCartItemByCartItemId(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("CartItem", "cartItemId", cartItemId));

        cart.getCartItems().remove(cartItem);
        cart.setTotalPrice(cart.getTotalPrice().subtract(BigDecimal.valueOf(cartItem.getQuantity()).multiply(BigDecimal.valueOf(cartItem.getQuantity()))));
        Cart savedCart = cartRepository.save(cart);


        List<CartItemDTO> cartItemDTOs = savedCart.getCartItems().stream()
                .map(item -> {
                    CartItemDTO itemDTO = new CartItemDTO();
                    itemDTO.setProductId(item.getProduct().getProductId());
                    itemDTO.setQuantity(item.getQuantity());
                    return itemDTO;
                }).toList();

        CartDTO cartDTO = new CartDTO();
        cartDTO.setItems(cartItemDTOs);
        cartDTO.setTotalPrice(savedCart.getTotalPrice());

        return cartDTO;
    }

    @Override
    public CartDTO clearCart(Long cartId) {
        User user = authUtils.loggedInUser();
        Cart userCart = cartRepository.findCartByUserAndCartId(user, cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "userId", user.getUserId()));

        cartRepository.delete(userCart);

        List<CartItemDTO> cartItemDTOS = userCart.getCartItems()
                .stream()
                .map(cartItem -> {
                    CartItemDTO cartItemDTO = new CartItemDTO();
                    cartItemDTO.setProductId(cartItem.getProduct().getProductId());
                    cartItemDTO.setQuantity(cartItem.getQuantity());
                    return cartItemDTO;
                }).toList();

        CartDTO deletedCartDTO = new CartDTO();
        deletedCartDTO.setItems(cartItemDTOS);
        deletedCartDTO.setTotalPrice(userCart.getTotalPrice());

        return deletedCartDTO;
    }

    @Override
    public CartDTO updateCartItem(Long cartItemId, CartItemDTO cartItemDTO) {
        User user = authUtils.loggedInUser();
        Cart userCart = cartRepository.findCartByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "userId", user.getUserId()));
        CartItem cartItem = cartItemRepository.findCartItemByCartAndProductId(userCart, cartItemId)
                .orElseThrow(() -> new ApiException("Product couldn't be found in the cart"));

        userCart.setTotalPrice(
                userCart.getTotalPrice()
                        .subtract(cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                        .add(cartItem.getPrice().multiply(BigDecimal.valueOf(cartItemDTO.getQuantity())))
        );
        Cart updatedCart = cartRepository.save(userCart);

        cartItem.setQuantity(cartItemDTO.getQuantity());
        cartItemRepository.save(cartItem);

        List<CartItemDTO> cartItemDTOS = updatedCart.getCartItems()
                .stream()
                .map(item -> {
                    CartItemDTO itemDTO = new CartItemDTO();
                    itemDTO.setProductId(item.getProduct().getProductId());
                    itemDTO.setQuantity(item.getQuantity());
                    return itemDTO;
                }).toList();

        CartDTO updatedCartDTO = new CartDTO();
        updatedCartDTO.setItems(cartItemDTOS);
        updatedCartDTO.setTotalPrice(updatedCart.getTotalPrice());

        return updatedCartDTO;
    }
}

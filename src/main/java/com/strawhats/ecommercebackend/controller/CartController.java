package com.strawhats.ecommercebackend.controller;

import com.strawhats.ecommercebackend.payload.CartDTO;
import com.strawhats.ecommercebackend.payload.CartItemDTO;
import com.strawhats.ecommercebackend.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "JWT Authorization")
@Tag(name = "\uD83D\uDED2 Cart Management Module", description = "APIs & Rest Endpoints related to Cart Management")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users/{userId}/cart")
public class CartController {

    private final CartService cartService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "Product Not Found"),
            @ApiResponse(responseCode = "400", description = "Bad Request(Quantity greater than Stock | Product already in Cart")
    })
    @Operation(summary = "Add to Cart", description = "Add product to user’s cart")
    @PostMapping
    public ResponseEntity<CartDTO> addToCart(
            @Parameter(name = "userId", required = true)
            @PathVariable Long userId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Cart Item object to be added to cart")
            @RequestBody CartItemDTO cartItemDTO
            ) {
        CartDTO cartDTO = cartService.addToCart(userId, cartItemDTO);
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "Cart Not Found")
    })
    @Operation(summary = "Get Cart", description = "Get items in user's cart")
    @GetMapping
    public ResponseEntity<CartDTO> getCart(
            @Parameter(name = "userId", required = true)
            @PathVariable Long userId
    ) {
        CartDTO cartDTO = cartService.getCart(userId);
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "Cart or Cart Item Not Found")
    })
    @Operation(summary = "Remove from Cart", description = "Remove a product from the cart")
    @DeleteMapping("/{itemId}")
    public ResponseEntity<CartDTO> removeFromCart(
            @Parameter(name = "userId", required = true)
            @PathVariable Long userId,
            @Parameter(name = "itemId",  required = true)
            @PathVariable Long itemId
    ) {
        CartDTO cartDTO = cartService.removeFromCart(userId, itemId);
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "Cart Not Found")
    })
    @Operation(summary = "Clear Cart", description = "Delete the speicified cart")
    @DeleteMapping("/clear")
    public ResponseEntity<CartDTO> clearCart(
            @Parameter(name = "userId",  required = true)
            @PathVariable Long userId
    ) {
       CartDTO deleteCartDTO = cartService.clearCart(userId);
       return new ResponseEntity<>(deleteCartDTO, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "Cart Not Found"),
            @ApiResponse(responseCode = "400", description = "Product Not Found in Cart")
    })
    @Operation(summary = "Update Cart Item", description = "Update item info")
    @PutMapping("/{itemId}")
    public ResponseEntity<CartDTO> updateCartItem(
            @Parameter(name = "itemId", required = true)
            @PathVariable Long itemId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Cart item object to be replaced", required = true)
            @RequestBody CartItemDTO cartItemDTO
    ) {
        CartDTO updatedCartDTO = cartService.updateCartItem(itemId, cartItemDTO);
        return new ResponseEntity<>(updatedCartDTO, HttpStatus.OK);
    }
}

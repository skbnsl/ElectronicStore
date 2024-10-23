package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.dtos.AddItemToCartRequest;
import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.CartDto;
import com.lcwd.electronic.store.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    //add items to cart
    @PreAuthorize("hasAnyRole('ADMIN','NORMAL')")
    @PostMapping("/{userId}")
    public ResponseEntity<CartDto> addItemToCart(@PathVariable String userId, @RequestBody AddItemToCartRequest request){
            CartDto cartDto = cartService.addItemsToCart(userId, request);
            return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

    //remove item from cart
    @PreAuthorize("hasAnyRole('ADMIN','NORMAL')")
    @DeleteMapping("/{userId}/items/{itemId}")
    public ResponseEntity<ApiResponseMessage> removeItemFromCart(
            @PathVariable int itemId,
            @PathVariable String userId
    ){
        cartService.removeItemFromCart(userId, itemId);
        ApiResponseMessage response = ApiResponseMessage.builder()
                .message("item is removed!!")
                .success(true)
                .status(HttpStatus.OK)
                .build();

        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    //clear cart
    @PreAuthorize("hasAnyRole('ADMIN','NORMAL')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> clearCart(
            @PathVariable String userId
    ){
        cartService.clearCart(userId);
        ApiResponseMessage response = ApiResponseMessage.builder()
                .message("Now! Cart is blank!!")
                .success(true)
                .status(HttpStatus.OK)
                .build();

        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    //get cart
    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getCart(@PathVariable String userId){
        CartDto cartDto = cartService.getCartByUser(userId);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }


}

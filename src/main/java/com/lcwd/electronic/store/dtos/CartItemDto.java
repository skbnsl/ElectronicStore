package com.lcwd.electronic.store.dtos;

import com.lcwd.electronic.store.entities.Cart;
import com.lcwd.electronic.store.entities.CartItem;
import com.lcwd.electronic.store.entities.Product;
import com.lcwd.electronic.store.entities.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CartItemDto {

    private int cartItemId;


    private ProductDto product;

    private int quantity;

    private int totalPrice;

}

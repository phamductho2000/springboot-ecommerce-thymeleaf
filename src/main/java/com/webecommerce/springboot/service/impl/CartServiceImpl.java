package com.webecommerce.springboot.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.webecommerce.springboot.dto.CartDTO;
import com.webecommerce.springboot.service.CartService;
import com.webecommerce.springboot.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    ProductService productService;

    @Override
    public HashMap<String, CartDTO> addToCart(String id, int quantity, HashMap<String, CartDTO> cart) throws JsonProcessingException {
        CartDTO itemCart;
        if (cart.containsKey(id)) {
            itemCart = cart.get(id);
        } else {
            itemCart = new CartDTO();
            itemCart.setProduct(productService.findById(id));
        }
        itemCart.setQuantity(itemCart.getQuantity() + quantity);
        itemCart.setTotalPrice(itemCart.getQuantity() * itemCart.getProduct().getPrice());
        cart.put(id, itemCart);
        return cart;
    }

    @Override
    public HashMap<String, CartDTO> updateCart(String key, int quantity, HashMap<String, CartDTO> cart) {
        cart.get(key).setQuantity(quantity);
        cart.get(key).setTotalPrice(cart.get(key).getProduct().getPrice() * quantity);
        return cart;
    }

    @Override
    public HashMap<String, CartDTO> removeItemFromCart(String key, HashMap<String, CartDTO> cart) {
        cart.remove(key);
        return cart;
    }

    @Override
    public Double calcTotalPrice(HashMap<String, CartDTO> cart) {
        return cart.values().stream()
                .mapToDouble(CartDTO::getTotalPrice).sum();
    }

}

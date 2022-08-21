package com.webecommerce.springboot.service.impl;

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
    public HashMap<Long, CartDTO> addToCart(Long id, int quantity, HashMap<Long, CartDTO> cart) {
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
    public HashMap<Long, CartDTO> updateCart(Long key, int quantity, HashMap<Long, CartDTO> cart) {
        cart.get(key).setQuantity(quantity);
        cart.get(key).setTotalPrice(cart.get(key).getProduct().getPrice() * quantity);
        return cart;
    }

    @Override
    public HashMap<Long, CartDTO> removeItemFromCart(Long key, HashMap<Long, CartDTO> cart) {
        cart.remove(key);
        return cart;
    }

    @Override
    public Double calcTotalPrice(HashMap<Long, CartDTO> cart) {
        return cart.values().stream()
                .mapToDouble(CartDTO::getTotalPrice).sum();
    }

}

package com.webecommerce.springboot.service;

import com.webecommerce.springboot.dto.CartDTO;

import java.util.HashMap;

public interface CartService {

    HashMap<Long, CartDTO> addToCart(Long id, int quantity, HashMap<Long, CartDTO> cart);

    HashMap<Long, CartDTO> updateCart(Long key, int quantity, HashMap<Long, CartDTO> cart);

    HashMap<Long, CartDTO> removeItemFromCart(Long key, HashMap<Long, CartDTO> cart);

    Double calcTotalPrice(HashMap<Long, CartDTO> cart);
}

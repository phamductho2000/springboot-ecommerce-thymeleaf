package com.webecommerce.springboot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.webecommerce.springboot.dto.CartDTO;

import java.util.HashMap;

public interface CartService {

    HashMap<String, CartDTO> addToCart(String id, int quantity, HashMap<String, CartDTO> cart) throws JsonProcessingException;

    HashMap<String, CartDTO> updateCart(String key, int quantity, HashMap<String, CartDTO> cart);

    HashMap<String, CartDTO> removeItemFromCart(String key, HashMap<String, CartDTO> cart);

    Double calcTotalPrice(HashMap<String, CartDTO> cart);
}

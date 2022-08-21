package com.webecommerce.springboot.service;

import com.webecommerce.springboot.dto.CartDTO;
import com.webecommerce.springboot.dto.FormAddOrderDTO;
import com.webecommerce.springboot.dto.OrderDTO;

import java.util.HashMap;
import java.util.List;

public interface OrderService {
    List<OrderDTO> findAll();

    OrderDTO findById(Long id);

    OrderDTO save(String userEmail, Long addressId, HashMap<Long, CartDTO> cart, Double totalOrderPrice, Double feeShip);

    OrderDTO adminSave(FormAddOrderDTO formAddOrderDTO);

    OrderDTO adminUpdate(FormAddOrderDTO formAddOrderDTO);

    void updateStatus(Long id, int status);

    List<OrderDTO> findAllByUserEmail(String email);
}

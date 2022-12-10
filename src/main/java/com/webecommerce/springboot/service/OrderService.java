package com.webecommerce.springboot.service;

import com.webecommerce.springboot.dto.CartDTO;
import com.webecommerce.springboot.dto.FormAddOrderDTO;
import com.webecommerce.springboot.dto.OrderDTO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public interface OrderService {
    List<OrderDTO> findAll();

    OrderDTO findById(String id);

    OrderDTO save(String userEmail, Long addressId, HashMap<String, CartDTO> cart, long totalOrderPrice, long feeShip);

    OrderDTO adminSave(FormAddOrderDTO formAddOrderDTO);

    OrderDTO adminUpdate(FormAddOrderDTO formAddOrderDTO);

    void updateStatus(String id, int status);

    List<OrderDTO> findAllByUserEmail(String email);

    void exportExcel(HttpServletResponse response, List<String> ids) throws IOException;
}

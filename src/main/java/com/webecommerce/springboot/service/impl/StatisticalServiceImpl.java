package com.webecommerce.springboot.service.impl;

import com.webecommerce.springboot.dto.RevenuesAndBenefitByYearDTO;
import com.webecommerce.springboot.dto.RevenuesAndBenefitDTO;
import com.webecommerce.springboot.dto.TopSellingProductDTO;
import com.webecommerce.springboot.repository.OrderRepository;
import com.webecommerce.springboot.service.OrderService;
import com.webecommerce.springboot.service.StatisticalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticalServiceImpl implements StatisticalService {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Override
    public RevenuesAndBenefitDTO getRevenueAndBenefit() {
        return orderRepository.getRevenueAndBenefit();
    }

    @Override
    public List<RevenuesAndBenefitByYearDTO> getRevenueAndBenefitByYear(int year) {
        return orderRepository.getRevenueAndBenefitByYear(year);
    }

    @Override
    public List<TopSellingProductDTO> getTopSellingProduct() {
        return orderRepository.getTopSellingProduct();
    }
}

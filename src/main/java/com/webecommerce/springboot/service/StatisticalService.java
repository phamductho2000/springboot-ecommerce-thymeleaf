package com.webecommerce.springboot.service;

import com.webecommerce.springboot.dto.RevenuesAndBenefitByYearDTO;
import com.webecommerce.springboot.dto.RevenuesAndBenefitDTO;
import com.webecommerce.springboot.dto.TopSellingProductDTO;

import java.util.List;

public interface StatisticalService {
    RevenuesAndBenefitDTO getRevenueAndBenefit();

    List<RevenuesAndBenefitByYearDTO> getRevenueAndBenefitByYear(int year);

    List<TopSellingProductDTO> getTopSellingProduct();
}

package com.webecommerce.springboot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RevenuesAndBenefitByYearDTO {

    private int year;

    private int month;

    private double revenues;

    private double benefit;
}

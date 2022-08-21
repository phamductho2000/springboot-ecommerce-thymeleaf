package com.webecommerce.springboot.controller.admin;

import com.webecommerce.springboot.dto.RevenuesAndBenefitByYearDTO;
import com.webecommerce.springboot.service.CustomerSevice;
import com.webecommerce.springboot.service.OrderService;
import com.webecommerce.springboot.service.StatisticalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class DashboardController {

    @Autowired
    OrderService orderService;

    @Autowired
    StatisticalService statisticalService;

    @Autowired
    CustomerSevice customerSevice;

    @GetMapping("/dashboard")
    public String dashboardPage(Model model) {
        model.addAttribute("countCustomers", customerSevice.findAllCustomer().size());
        model.addAttribute("countOrders", orderService.findAll().size());
        model.addAttribute("revenueAndBenefit", statisticalService.getRevenueAndBenefit());
        model.addAttribute("topSellingProducts", statisticalService.getTopSellingProduct());
        return "admin/dashboard";
    }

    @GetMapping("/getRevenueAndBenefitByYear/{year}")
    @ResponseBody
    public List<RevenuesAndBenefitByYearDTO> getRevenueAndBenefitByYear(@PathVariable int year) {
        return statisticalService.getRevenueAndBenefitByYear(year);
    }
}

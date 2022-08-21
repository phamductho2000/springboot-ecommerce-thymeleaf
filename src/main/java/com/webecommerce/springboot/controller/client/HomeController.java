package com.webecommerce.springboot.controller.client;

import com.webecommerce.springboot.service.CategoryService;
import com.webecommerce.springboot.service.ProductService;
import com.webecommerce.springboot.service.StatisticalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/home")
@Controller
public class HomeController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    StatisticalService statisticalService;

    @Autowired
    ProductService productService;

    @GetMapping("")
    public String home(Model model) {
        model.addAttribute("menuItems", categoryService.listWithTree());
        model.addAttribute("topSellProducts", productService.findAllProducts(PageRequest.of(1,
                10, Sort.by("sellCount").descending())));
        return "client/test";
    }
}

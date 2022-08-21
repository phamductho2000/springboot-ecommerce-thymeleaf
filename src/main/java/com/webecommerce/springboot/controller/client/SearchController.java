package com.webecommerce.springboot.controller.client;

import com.webecommerce.springboot.service.CategoryService;
import com.webecommerce.springboot.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/san-pham")
public class SearchController {

    @Autowired
    ProductService productService;

    @Autowired
    CategoryService categoryService;

    @GetMapping("")
    public String searchProduct(@RequestParam(value = "key") String keyWord, Model model) {
        model.addAttribute("menuItems", categoryService.listWithTree());
        model.addAttribute("listProducts", productService.findAllProductsByName(keyWord));
        return "client/search";
    }
}

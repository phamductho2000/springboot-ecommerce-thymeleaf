package com.webecommerce.springboot.controller.client;

import com.webecommerce.springboot.dto.ProductDTO;
import com.webecommerce.springboot.service.AttributeService;
import com.webecommerce.springboot.service.CategoryService;
import com.webecommerce.springboot.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ProductController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    ProductService productService;

    @Autowired
    AttributeService attributeService;

    @GetMapping("/san-pham/{id}")
    public String index(@PathVariable Long id, Model model) {
        ProductDTO p = productService.findById(id);
        model.addAttribute("menuItems", categoryService.listWithTree());
        model.addAttribute("product", productService.findById(id));
        return "client/product";
    }

}

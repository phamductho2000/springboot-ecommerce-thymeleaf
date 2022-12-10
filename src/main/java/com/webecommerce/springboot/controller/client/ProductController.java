package com.webecommerce.springboot.controller.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.webecommerce.springboot.dto.ProductDTO;
import com.webecommerce.springboot.service.AttributeService;
import com.webecommerce.springboot.service.CategoryService;
import com.webecommerce.springboot.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class ProductController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    ProductService productService;

    @Autowired
    AttributeService attributeService;

    @GetMapping("/san-pham/{name}")
    public String index(@PathVariable String name, Model model, @RequestParam Map<String, String> params) throws JsonProcessingException {
        model.addAttribute("menuItems", categoryService.listWithTree());
        model.addAttribute("product", productService.findById(params.get("id")));
        return "client/product";
    }

}

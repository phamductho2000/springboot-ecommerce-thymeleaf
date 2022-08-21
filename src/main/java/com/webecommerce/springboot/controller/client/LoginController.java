package com.webecommerce.springboot.controller.client;

import com.webecommerce.springboot.service.CategoryService;
import com.webecommerce.springboot.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dang-nhap")
public class LoginController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    ProductService productService;

    @GetMapping("")
    public String indexPage(Model model) {
        model.addAttribute("menuItems", categoryService.listWithTree());
        return "client/login";
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("menuItems", categoryService.listWithTree());
        model.addAttribute("topSellProducts", productService.findAllProducts(PageRequest.of(1,
                10, Sort.by("sellCount").descending())));
        return "client/index";
    }
}

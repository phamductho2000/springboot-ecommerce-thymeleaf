package com.webecommerce.springboot.controller.client;

import com.webecommerce.springboot.dto.AttributeAndValueFilterDTO;
import com.webecommerce.springboot.dto.ProductDTO;
import com.webecommerce.springboot.entity.ProductEntity;
import com.webecommerce.springboot.service.AttributeService;
import com.webecommerce.springboot.service.CategoryService;
import com.webecommerce.springboot.service.ProductService;
import com.webecommerce.springboot.specification.ProductSearchCriteria;
import com.webecommerce.springboot.specification.ProductSpecificationsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

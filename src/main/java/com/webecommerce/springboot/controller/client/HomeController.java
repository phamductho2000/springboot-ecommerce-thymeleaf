package com.webecommerce.springboot.controller.client;

import com.webecommerce.springboot.dto.UploadFoldersDTO;
import com.webecommerce.springboot.service.CategoryService;
import com.webecommerce.springboot.service.ProductService;
import com.webecommerce.springboot.service.StatisticalService;
import com.webecommerce.springboot.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;

@RequestMapping("")
@Controller
public class HomeController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    StatisticalService statisticalService;

    @Autowired
    ProductService productService;

    @Autowired
    StorageService storageService;

    @GetMapping("")
    public String home(Model model) throws IOException {
        model.addAttribute("menuItems", categoryService.listWithTree());
        model.addAttribute("topSellProducts", productService.findAllProducts(PageRequest.of(1,
                10, Sort.by("sellCount").descending())));
        List<UploadFoldersDTO> test = storageService.loadAllFolders();
        int a = 2;
        return "client/test";
    }
}

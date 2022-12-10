package com.webecommerce.springboot.controller.client;

import com.webecommerce.springboot.service.AttributeService;
import com.webecommerce.springboot.service.CategoryService;
import com.webecommerce.springboot.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class ShopController {

    @Autowired
    ProductService productService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    AttributeService attributeService;

    @GetMapping("/collections/**")
    public String shop(HttpServletRequest request,
                       Model model,
                       @RequestParam Map<String, String> params,
                       @RequestParam(value = "search") Optional<String> search,
                       @RequestParam Optional<Integer> page,
                       @RequestParam Optional<Integer> limit,
                       @RequestParam Optional<String> sort) {
        String path = ServletUriComponentsBuilder.fromRequestUri(request).host(null).scheme(null).query(null).build().toUriString().trim().substring(13);
        List<String> paths = Arrays.asList(path.split("/"));
        model.addAttribute("breadCrumbs",
                categoryService.findAllParentByChild(paths.get(paths.size() - 1)));
        model.addAttribute("filterCates",
                categoryService.findAllByParentId(paths.get(paths.size() - 1)));
        model.addAttribute("filterAttrs",
                attributeService.getAttrAndValueByCate(path));
        model.addAttribute("link", request.getRequestURI());
        model.addAttribute("menuItems", categoryService.listWithTree());
        model.addAttribute("listProducts", productService.search(params, page, limit, sort, path));
        model.addAttribute("maxAndMin", productService.getMinAndMaxPrice());
        int totalPages = productService.search(params, page, limit, sort, path).getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "client/shop";
    }
}

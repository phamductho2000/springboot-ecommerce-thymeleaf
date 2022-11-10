package com.webecommerce.springboot.controller.client;

import com.webecommerce.springboot.dto.ProductDTO;
import com.webecommerce.springboot.entity.ProductEntity;
import com.webecommerce.springboot.service.AttributeService;
import com.webecommerce.springboot.service.CategoryService;
import com.webecommerce.springboot.service.ProductService;
import com.webecommerce.springboot.specification.ProductSpecificationsBuilder;
import com.webecommerce.springboot.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        String path = request.getServletPath().trim().substring(13);
        Map<String, String> searchParams = Util.getSearchParams(params);
        Page<ProductDTO> listProducts = null;
        if (!searchParams.isEmpty()) {
            ProductSpecificationsBuilder builder = new ProductSpecificationsBuilder();
            searchParams.entrySet().stream()
                    .forEach(p -> builder.with(p.getKey(), "=", p.getValue()));
            Specification<ProductEntity> spec = builder.build();
            listProducts = productService.search(spec, PageRequest.of(page.orElse(1) - 1,
                    limit.orElse(6), Sort.by(sort.orElse("name"))),
                    path
            );
        } else {
            listProducts = productService.findAllProductsByCate(
                    PageRequest.of(page.orElse(1) - 1, limit.orElse(6), Sort.by(sort.orElse("name"))),
                    path
            );
        }
        List<String> paths = Arrays.asList(path.split("/"));
        model.addAttribute("filterCates",
                categoryService.findAllByParentId(paths.get(paths.size() - 1)));
        model.addAttribute("filterAttrs",
                attributeService.getAttrAndValueByCate(path));
        model.addAttribute("link", request.getRequestURI());
        model.addAttribute("menuItems", categoryService.listWithTree());
        model.addAttribute("listProducts", listProducts);
        model.addAttribute("maxAndMin", productService.getMinAndMaxPrice());
        int totalPages = listProducts.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "client/shop";
    }
}

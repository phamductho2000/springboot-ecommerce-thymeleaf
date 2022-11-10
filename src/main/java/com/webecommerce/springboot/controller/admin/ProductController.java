package com.webecommerce.springboot.controller.admin;

import com.webecommerce.springboot.dto.CategoryDTO;
import com.webecommerce.springboot.dto.ProductDTO;
import com.webecommerce.springboot.service.AttributeService;
import com.webecommerce.springboot.service.CategoryService;
import com.webecommerce.springboot.service.ProductService;
import com.webecommerce.springboot.service.TypeProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller(value = "productControllerOfAdmin")
@RequestMapping("/admin/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    AttributeService attributeService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    TypeProductService typeProductService;

    @GetMapping("")
    public String indexPage(Model model) {
        model.addAttribute("listProducts", productService.findAllProducts());
        return "admin/product/index";
    }

    @GetMapping("/add")
    public String showAddPage(Model model) {
        model.addAttribute("product", new ProductDTO());
        List<CategoryDTO> listCate  = categoryService.loadCategory(categoryService.findAll(), 0L, 0, "");
        model.addAttribute("listCategories", listCate);
        model.addAttribute("listTypesProduct", typeProductService.findAll());
        return "admin/product/add";
    }

    @PostMapping("/add")
    public String add(ProductDTO product, RedirectAttributes atts) {
        productService.save(product);
        atts.addFlashAttribute("message", "Thêm mới sản phẩm thành công");
        return "redirect:/admin/product";
    }

    @GetMapping("/edit/{id}")
    public String showEditPage(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.findById(id));
        List<CategoryDTO> listCate  = categoryService.loadCategory(categoryService.findAll(), 0L, 0, "");
        model.addAttribute("listAttributes", attributeService.findAll());
        model.addAttribute("listCategories", listCate);
        model.addAttribute("listTypesProduct", typeProductService.findAll());
        return "admin/product/edit";
    }

    @PostMapping("/update")
    public String edit(ProductDTO product, RedirectAttributes atts) {
        productService.save(product);
        atts.addFlashAttribute("message", "Cập nhật sản phẩm thành công");
        return "redirect:/admin/product";
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable Long id, RedirectAttributes atts) {
        productService.remove(id);
        atts.addFlashAttribute("message", "Xóa sản phẩm thành công");
        return "redirect:/admin/product";
    }

    @GetMapping("/attribute")
    public String loadAttribute(Model model) {
        model.addAttribute("listAttributes", attributeService.findAll());
        return "admin/product/attribute";
    }

    @GetMapping("/api/getInfo/{id}")
    @ResponseBody
    public ProductDTO getInfo(@PathVariable Long id) {
        return productService.findById(id);
    }
}

package com.webecommerce.springboot.controller.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.webecommerce.springboot.dto.CategoryDTO;
import com.webecommerce.springboot.dto.ProductDTO;
import com.webecommerce.springboot.service.*;
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

    @Autowired
    SavedIdService savedIdService;

    @GetMapping("")
    public String indexPage(Model model) {
        savedIdService.generateNewId("OD");
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
    public String add(ProductDTO product, RedirectAttributes atts) throws JsonProcessingException {
        productService.save(product);
        atts.addFlashAttribute("message", "Thêm mới sản phẩm thành công");
        return "redirect:/admin/product";
    }

    @GetMapping("/edit/{id}")
    public String showEditPage(@PathVariable String id, Model model) throws JsonProcessingException {
        model.addAttribute("product", productService.findById(id));
        List<CategoryDTO> listCate  = categoryService.loadCategory(categoryService.findAll(), 0L, 0, "");
        model.addAttribute("listAttributes", attributeService.findAll());
        model.addAttribute("listCategories", listCate);
        model.addAttribute("listTypesProduct", typeProductService.findAll());
        return "admin/product/edit";
    }

    @PostMapping("/update")
    public String edit(ProductDTO product, RedirectAttributes atts) throws JsonProcessingException {
        productService.save(product);
        atts.addFlashAttribute("message", "Cập nhật sản phẩm thành công");
        return "redirect:/admin/product";
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable String id, RedirectAttributes atts) {
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
    public ProductDTO getInfo(@PathVariable String id) throws JsonProcessingException {
        return productService.findById(id);
    }

    @GetMapping("/get-table-products")
    public String tableProductsPage(Model model) {
        model.addAttribute("listProducts", productService.findAllProducts());
        return "admin/product/table-product";
    }

    @PostMapping("/api/get-all-by-ids")
    @ResponseBody
    public List<ProductDTO> getAllByIds(@RequestBody List<String> ids) {
        return productService.findAllProductsByIds(ids);
    }

    @PostMapping("/api/get-all-by-slug-seo")
    @ResponseBody
    public List<ProductDTO> getAllBySlugSeo(@RequestBody String slugSeo) {
        return productService.findAllBySlugSeo(slugSeo);
    }
}

package com.webecommerce.springboot.controller.admin;

import com.webecommerce.springboot.dto.CategoryDTO;
import com.webecommerce.springboot.dto.TypeProductDTO;
import com.webecommerce.springboot.service.AttributeService;
import com.webecommerce.springboot.service.TypeProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/type-product")
public class TypeProductController {

    @Autowired
    TypeProductService typeProductService;

    @Autowired
    AttributeService attributeService;

    @GetMapping("")
    public String indexPage(Model model) {
        model.addAttribute("types", typeProductService.findAll());
        return "admin/type-product/index";
    }

    @GetMapping("/add")
    public String addPage(Model model) {
        model.addAttribute("type", new TypeProductDTO());
        model.addAttribute("attrs", attributeService.findAll());
        return "admin/type-product/add";
    }

    @PostMapping("/save")
    public String add(TypeProductDTO typeProductDTO, RedirectAttributes atts) {
        typeProductService.save(typeProductDTO);
        atts.addFlashAttribute("message", "Thêm mới loại sản phẩm thành công");
        return "redirect:/admin/type-product";
    }

    @GetMapping("/edit/{id}")
    public String editPage(Model model, @PathVariable Long id) {
        model.addAttribute("type", typeProductService.findById(id));
        model.addAttribute("attrs", attributeService.findAll());
        return "admin/type-product/edit";
    }

    @GetMapping("/api/get-all")
    @ResponseBody
    public List<TypeProductDTO> findAll() {
        return typeProductService.findAll();
    }

}

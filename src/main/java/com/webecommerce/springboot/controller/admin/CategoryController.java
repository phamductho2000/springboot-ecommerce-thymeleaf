package com.webecommerce.springboot.controller.admin;

import com.webecommerce.springboot.dto.CategoryDTO;
import com.webecommerce.springboot.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping("")
    public String index(Model model) {
        List<CategoryDTO> listCate  = categoryService.loadCategory(categoryService.findAll(), 0L, 0, "");
        model.addAttribute("listCate", listCate);
        return "admin/category/index";
    }

    @GetMapping("/add")
    public String addPage(Model model) {
        List<CategoryDTO> listCate  = categoryService.loadCategory(categoryService.findAll(), 0L, 0, "");
        model.addAttribute("cate", new CategoryDTO());
        model.addAttribute("listCategories", listCate);
        return "admin/category/add";
    }

    @PostMapping("/save")
    public String add(CategoryDTO categoryDTO, RedirectAttributes atts) {
        categoryService.save(categoryDTO);
        atts.addFlashAttribute("message", "Thêm mới danh mục thành công");
        return "redirect:/admin/category";
    }

    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable Long id, Model model) {
        List<CategoryDTO> listCate  = categoryService.loadCategory(categoryService.findAll(), 0L, 0, "");
        model.addAttribute("cate", categoryService.findById(id));
        model.addAttribute("listCategories", listCate);
        return "admin/category/edit";
    }

    @PostMapping("/update")
    public String edit(CategoryDTO categoryDTO, RedirectAttributes atts) {
        categoryService.save(categoryDTO);
        atts.addFlashAttribute("message", "Cập nhật danh mục thành công");
        return "redirect:/admin/category";
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable Long id, RedirectAttributes atts) {
        categoryService.remove(id);
        atts.addFlashAttribute("message", "Xóa danh mục thành công");
        return "redirect:/admin/category";
    }
}

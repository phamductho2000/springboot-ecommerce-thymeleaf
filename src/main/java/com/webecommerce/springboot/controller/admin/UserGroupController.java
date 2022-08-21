package com.webecommerce.springboot.controller.admin;

import com.webecommerce.springboot.dto.RoleDTO;
import com.webecommerce.springboot.dto.UserDTO;
import com.webecommerce.springboot.service.PermissionService;
import com.webecommerce.springboot.service.RoleService;
import com.webecommerce.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/user-group")
public class UserGroupController {

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    PermissionService permissionService;

    @GetMapping("")
    public String indexPage(Model model) {
        model.addAttribute("roles", roleService.findAllRoleAdmin());
        return "/admin/user-group/index";
    }

    @GetMapping("/add")
    public String addPage(Model model) {
        model.addAttribute("role", new RoleDTO());
        model.addAttribute("permissions", permissionService.renderAll());
        return "/admin/user-group/add";
    }

    @PostMapping("/add")
    public String add(RoleDTO roleDTO, @RequestParam("perIds") Long[] perIds, RedirectAttributes atts) {
        roleService.save(roleDTO, perIds);
        atts.addFlashAttribute("message", "Xóa nhóm tài khoản thành công");
        return "redirect:/admin/user-group/";
    }

    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable Long id, Model model) {
        model.addAttribute("permissions", permissionService.renderAll());
        model.addAttribute("roleEdit", roleService.findById(id));
        return "/admin/user-group/edit";
    }

    @PostMapping("/update")
    public String edit(RoleDTO roleDTO, @RequestParam("perIds") Long[] perIds, RedirectAttributes atts) {
        roleService.save(roleDTO, perIds);
        atts.addFlashAttribute("message", "Cập nhật nhóm tài khoản thành công");
        return "redirect:/admin/user-group/";
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable Long id, RedirectAttributes atts) {
        roleService.remove(id);
        atts.addFlashAttribute("message", "Xóa nhóm tài khoản thành công");
        return "redirect:/admin/user-group/";
    }
}

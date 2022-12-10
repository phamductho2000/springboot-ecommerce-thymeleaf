package com.webecommerce.springboot.controller.admin;

import com.webecommerce.springboot.dto.DeliveryAddressDTO;
import com.webecommerce.springboot.dto.UserDTO;
import com.webecommerce.springboot.service.DeliveryAddressService;
import com.webecommerce.springboot.service.RoleService;
import com.webecommerce.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    DeliveryAddressService deliveryAddressService;

    @GetMapping("")
    public String indexPage(Model model) {
        model.addAttribute("listUsers", userService.findAllAccount());
        return "/admin/user/index";
    }

    @GetMapping("/add")
    public String addPage(Model model) {
        model.addAttribute("user", new UserDTO());
        model.addAttribute("roles", roleService.findAllRole());
        return "/admin/user/add";
    }

    @PostMapping("/add")
    public String add(UserDTO userDTO, RedirectAttributes atts) {
        userService.save(userDTO);
        atts.addFlashAttribute("message", "Thêm mới tài khoản thành công");
        return "redirect:/admin/user";
    }

    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable String id,  Model model) {
        model.addAttribute("user", userService.findById(id));
        model.addAttribute("roles", roleService.findAllRole());
        return "/admin/user/edit";
    }

    @PostMapping("/update")
    public String update(UserDTO userDTO, RedirectAttributes atts) {
        userService.update(userDTO);
        atts.addFlashAttribute("message", "Cập nhật tài khoản thành công");
        return "redirect:/admin/user";
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable String id, RedirectAttributes atts) {
        userService.remove(id);
        atts.addFlashAttribute("message", "Xóa tài khoản thành công");
        return "redirect:/admin/user";
    }

    @GetMapping("/api/getAddresses/{userId}")
    @ResponseBody
    public List<DeliveryAddressDTO> getAddresses(@PathVariable Long userId) {
        return deliveryAddressService.findAllByUserId(userId);
    }

    @GetMapping("/api/getInfo/{userId}")
    @ResponseBody
    public UserDTO getInfo(@PathVariable String userId) {
        return userService.findById(userId);
    }
}

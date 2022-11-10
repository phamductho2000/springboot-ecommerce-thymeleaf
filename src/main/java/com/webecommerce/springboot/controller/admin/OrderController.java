package com.webecommerce.springboot.controller.admin;

import com.webecommerce.springboot.dto.FormAddOrderDTO;
import com.webecommerce.springboot.service.OrderService;
import com.webecommerce.springboot.service.ProductService;
import com.webecommerce.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;

    @GetMapping("")
    public String indexPage(Model model) {
        model.addAttribute("orders", orderService.findAll());
        return "admin/order/index";
    }

    @GetMapping("/view/{id}")
    public String viewPage(@PathVariable Long id, Model model) {
        model.addAttribute("order", orderService.findById(id));
        return "admin/order/view";
    }

    @GetMapping("/add")
    public String addPage(Model model) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("products", productService.findAllProducts());
        return "admin/order/add";
    }

    @GetMapping("/edit/{id}")
    public String editPage(Model model, @PathVariable Long id) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("products", productService.findAllProducts());
        model.addAttribute("order", orderService.findById(id));
        return "admin/order/edit";
    }

    @PostMapping("/save")
    public String add(FormAddOrderDTO formAddOrderDTO, RedirectAttributes atts) {
        orderService.adminSave(formAddOrderDTO);
        atts.addFlashAttribute("message", "Thêm mới hóa đơn thành công");
        return "redirect:/admin/order";
    }

    @PostMapping("/update")
    public String update(FormAddOrderDTO formAddOrderDTO, RedirectAttributes atts) {
        orderService.adminUpdate(formAddOrderDTO);
        atts.addFlashAttribute("message", "Thêm mới sản phẩm thành công");
        return "redirect:/admin/order";
    }

    @PostMapping("/updateStatus")
    public String updateStatus(@RequestParam("orderId") Long id, @RequestParam("statusOrder") int status, RedirectAttributes atts) {
        orderService.updateStatus(id, status);
        atts.addFlashAttribute("message", "Cập nhật trạng thái đơn hàng thành công");
        return "redirect:/admin/order";
    }
}

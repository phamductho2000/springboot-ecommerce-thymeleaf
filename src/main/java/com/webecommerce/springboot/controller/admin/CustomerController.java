package com.webecommerce.springboot.controller.admin;

import com.webecommerce.springboot.dto.UserDTO;
import com.webecommerce.springboot.service.CustomerSevice;
import com.webecommerce.springboot.service.DeliveryAddressService;
import com.webecommerce.springboot.service.OrderService;
import com.webecommerce.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/customer")
public class CustomerController {

    @Autowired
    CustomerSevice customerSevice;

    @Autowired
    UserService userService;

    @Autowired
    OrderService orderService;

    @Autowired
    DeliveryAddressService deliveryAddressService;

    @GetMapping("")
    public String indexPage(Model model) {
        model.addAttribute("customers", customerSevice.findAllCustomer());
        return "admin/customer/index";
    }

    @GetMapping("/add")
    public String addPage(Model model) {
        model.addAttribute("customer", new UserDTO());
        return "admin/customer/add";
    }

    @GetMapping("/view/{id}")
    public String viewPage(@PathVariable Long id,  Model model) {
        UserDTO userDTO = userService.findById(id);
        model.addAttribute("customer", userDTO);
        model.addAttribute("addresses", deliveryAddressService.findAllByUserEmail(userDTO.getEmail()));
        model.addAttribute("orders", orderService.findAllByUserEmail(userDTO.getEmail()));
        return "admin/customer/view";
    }
}

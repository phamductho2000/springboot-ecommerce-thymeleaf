package com.webecommerce.springboot.controller.client;

import com.webecommerce.springboot.dto.MyUserDetails;
import com.webecommerce.springboot.security.oauth.CustomOAuth2User;
import com.webecommerce.springboot.service.CategoryService;
import com.webecommerce.springboot.service.DeliveryAddressService;
import com.webecommerce.springboot.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller(value = "userControllerOfClient")
@RequestMapping("/tai-khoan")
public class UserController {

    @Autowired
    OrderService orderService;

    @Autowired
    DeliveryAddressService deliveryAddressService;

    @Autowired
    CategoryService categoryService;

    @GetMapping("/thong-tin-tai-khoan")
    public String indexPage(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof MyUserDetails) {

        } else {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) principal;
            model.addAttribute("typeUser", "oauth2");
            model.addAttribute("userInfo", oAuth2User);
            model.addAttribute("addresses", deliveryAddressService.findAllByUserEmail(oAuth2User.getEmail()));
            model.addAttribute("orders", orderService.findAllByUserEmail(oAuth2User.getEmail()));
        }
        model.addAttribute("menuItems", categoryService.listWithTree());
        return "client/user/index";
    }
}

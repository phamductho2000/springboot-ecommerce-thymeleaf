package com.webecommerce.springboot.controller.client;

import com.webecommerce.springboot.dto.CartDTO;
import com.webecommerce.springboot.dto.MyUserDetails;
import com.webecommerce.springboot.dto.OrderDTO;
import com.webecommerce.springboot.security.oauth.CustomOAuth2User;
import com.webecommerce.springboot.service.CategoryService;
import com.webecommerce.springboot.service.DeliveryAddressService;
import com.webecommerce.springboot.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

@Controller
public class CheckoutController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    OrderService orderService;

    @Autowired
    DeliveryAddressService deliveryAddressService;

    @GetMapping("/thanh-toan")
    public String checkoutPage(HttpSession session, Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof MyUserDetails) {

        } else {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) principal;
            model.addAttribute("addresses", deliveryAddressService.findAllByUserEmail(oAuth2User.getEmail()));
        }
        model.addAttribute("menuItems", categoryService.listWithTree());
        session.setAttribute("TOTAL_ORDER_PRICE", (Double) session.getAttribute("TOTAL_CART_PRICE"));
        return "client/checkout";
    }

    @PostMapping("/update-total-price-order")
    @ResponseBody
    public Double updateTotalPriceOrder(HttpSession session, @RequestParam("feeShip") double feeShip) {
        Long totalPrice = (Long) session.getAttribute("TOTAL_CART_PRICE");
        Long newTotalPrice = (Long) session.getAttribute("TOTAL_ORDER_PRICE");
        if (newTotalPrice == null) {
            newTotalPrice = totalPrice;
            session.setAttribute("TOTAL_ORDER_PRICE", totalPrice);
        }
        session.setAttribute("TOTAL_ORDER_PRICE", totalPrice + feeShip);
        return totalPrice + feeShip;
    }

    @PostMapping("/thanh-toan")
    @ResponseBody
    public OrderDTO checkout(HttpSession session, @RequestParam("addressId") Long addressId) {
        Long totalOrderPrice = (Long) session.getAttribute("TOTAL_ORDER_PRICE");
        Long totalCartPrice = (Long) session.getAttribute("TOTAL_CART_PRICE");
        HashMap<String, CartDTO> cart = (HashMap<String, CartDTO>) session.getAttribute("CART");
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof MyUserDetails) {
            return null;
        }
        CustomOAuth2User oAuth2User = (CustomOAuth2User) principal;
        OrderDTO orderDTO = orderService.save(oAuth2User.getEmail(), addressId, cart, totalOrderPrice, totalOrderPrice - totalCartPrice);
        session.removeAttribute("TOTAL_ORDER_PRICE");
        session.removeAttribute("TOTAL_CART_PRICE");
        session.removeAttribute("CART");
        return orderDTO;
    }
}

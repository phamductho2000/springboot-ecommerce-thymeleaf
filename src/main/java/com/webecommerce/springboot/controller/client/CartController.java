package com.webecommerce.springboot.controller.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.webecommerce.springboot.dto.CartDTO;
import com.webecommerce.springboot.service.CartService;
import com.webecommerce.springboot.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

@Controller
public class CartController {

    @Autowired
    CartService cartService;

    @Autowired
    CategoryService categoryService;

    @GetMapping("/gio-hang")
    public String home(Model model, HttpSession session) {
        HashMap<String, CartDTO> cart = (HashMap<String, CartDTO>) session.getAttribute("CART");
        model.addAttribute("menuItems", categoryService.listWithTree());
//        model.addAttribute("total", cartService.calcTotalPrice(cart));
        return "client/cart";
    }

    @PostMapping("/them-vao-gio-hang/{id}")
    public String addToCart(HttpSession session, @PathVariable String id, @RequestParam("quantity") int quantity) throws JsonProcessingException {
        HashMap<String, CartDTO> cart = (HashMap<String, CartDTO>) session.getAttribute("CART");
        if (cart == null) {
            cart = new HashMap<>();
        }
        cart = cartService.addToCart(id, quantity, cart);
        session.setAttribute("CART", cart);
        session.setAttribute("TOTAL_CART_PRICE", cartService.calcTotalPrice(cart));
        return "redirect:/gio-hang";
    }

    @PostMapping("/cap-nhat-gio-hang/{id}")
    public String updateCart(HttpSession session, @PathVariable String id, @RequestParam("quantity") int quantity) {
        HashMap<String, CartDTO> cart = (HashMap<String, CartDTO>) session.getAttribute("CART");
        if (cart == null) {
            cart = new HashMap<>();
        }
        cart = cartService.updateCart(id, quantity, cart);
        session.setAttribute("CART", cart);
        session.setAttribute("TOTAL_CART_PRICE", cartService.calcTotalPrice(cart));
        return "redirect:/gio-hang";
    }

    @PostMapping("/xoa-khoi-gio-hang/{id}")
    public String removeItemFromCart(HttpSession session, @PathVariable String id) {
        HashMap<String, CartDTO> cart = (HashMap<String, CartDTO>) session.getAttribute("CART");
        if (cart == null) {
            cart = new HashMap<>();
        }
        cart = cartService.removeItemFromCart(id, cart);
        if(!cart.isEmpty()) {
            session.setAttribute("CART", cart);
            session.setAttribute("TOTAL_CART_PRICE", cartService.calcTotalPrice(cart));
        }
        return "redirect:/gio-hang";
    }
}

package com.webecommerce.springboot.controller.client;

import com.webecommerce.springboot.dto.DeliveryAddressDTO;
import com.webecommerce.springboot.dto.MyUserDetails;
import com.webecommerce.springboot.security.oauth.CustomOAuth2User;
import com.webecommerce.springboot.service.DeliveryAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/delivery-address")
public class DeliveryAddressController {

    @Autowired
    DeliveryAddressService deliveryAddressService;

    @GetMapping("/{id}")
    @ResponseBody
    public DeliveryAddressDTO findById(@PathVariable Long id) {
        return deliveryAddressService.findById(id);
    }

    @PostMapping("/save")
    @ResponseBody
    public DeliveryAddressDTO saveAddress(DeliveryAddressDTO deliveryAddressDTO) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        DeliveryAddressDTO deliveryAddressDTO1 = new DeliveryAddressDTO();
        if(principal instanceof MyUserDetails) {

        } else {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) principal;
            deliveryAddressDTO1 = deliveryAddressService.save(oAuth2User.getEmail(), deliveryAddressDTO);
        }
        return deliveryAddressDTO1;
    }

    @PostMapping("/update/{id}")
    public String updateAddress(@PathVariable Long id, DeliveryAddressDTO deliveryAddressDTO) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        DeliveryAddressDTO deliveryAddressDTO1 = new DeliveryAddressDTO();
        if (principal instanceof MyUserDetails) {

        } else {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) principal;
            deliveryAddressDTO1 = deliveryAddressService.update(id, oAuth2User.getEmail(), deliveryAddressDTO);
        }
        return "redirect:/thanh-toan";
    }

    @PostMapping("/user/update/{id}")
    public String updateAddressUser(@PathVariable Long id, DeliveryAddressDTO deliveryAddressDTO) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        DeliveryAddressDTO deliveryAddressDTO1 = new DeliveryAddressDTO();
        if (principal instanceof MyUserDetails) {

        } else {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) principal;
            deliveryAddressDTO1 = deliveryAddressService.update(id, oAuth2User.getEmail(), deliveryAddressDTO);
        }
        return "redirect:/tai-khoan/thong-tin-tai-khoan";
    }


}

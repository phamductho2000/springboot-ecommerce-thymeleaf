package com.webecommerce.springboot.controller.client;

import com.webecommerce.springboot.dto.DeliveryAddressDTO;
import com.webecommerce.springboot.dto.MyUserDetails;
import com.webecommerce.springboot.security.oauth.CustomOAuth2User;
import com.webecommerce.springboot.service.DeliveryAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/address")
public class AddressController {

    @Autowired
    DeliveryAddressService deliveryAddressService;

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
}

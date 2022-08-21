package com.webecommerce.springboot.controller.admin;

import com.webecommerce.springboot.dto.ImageDTO;
import com.webecommerce.springboot.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/upload")
public class StorageImageController {

    @Autowired
    StorageService storageService;

    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("listImages", storageService.loadAll());
        return "admin/file_manager/index";
    }

    @GetMapping("/getAllFromDb")
    @ResponseBody
    public List<ImageDTO> getAll() {
        return storageService.loadAllFromDb();
    }

    @PostMapping("")
    public String upload(MultipartFile file) {
        storageService.storeFile(file);
        return "redirect:/admin/upload";
    }
}

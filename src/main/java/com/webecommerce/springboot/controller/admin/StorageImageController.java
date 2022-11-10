package com.webecommerce.springboot.controller.admin;

import com.webecommerce.springboot.dto.ImageDTO;
import com.webecommerce.springboot.dto.UploadFoldersDTO;
import com.webecommerce.springboot.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin/file-manager")
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
    public List<ImageDTO> getAllFromDb() {
        return storageService.loadAllFromDb();
    }

    @GetMapping("/getAllFolder")
    @ResponseBody
    public List<UploadFoldersDTO> getAllFolder() throws IOException {
        return storageService.loadAllFolders();
    }

//    @PostMapping("")
//    public String upload(MultipartFile file) {
//        storageService.storeFile(file);
//        return "redirect:/admin/upload";
//    }

    @GetMapping("/home")
    public String homePage(Model model) throws IOException {
        model.addAttribute("folders", storageService.loadAllFolders());
        model.addAttribute("listImages", storageService.loadAllFromDb());
        return "admin/file_manager/file-manager";
    }

    @PostMapping("/upload-img")
    @ResponseBody
    public List<ImageDTO> uploadTest(@RequestParam("file") MultipartFile file, @RequestParam("path") String path) {
        return storageService.storeFile(file, path);
//        return "redirect:/admin/file-manager/home";
    }

    @PostMapping("/create-sub-folder")
    public String createSubFolder(@RequestParam("parentPath") String parentPath,
                                  @RequestParam("folderName") String folderName) {
        storageService.createSubFolder(parentPath, folderName);
        return "redirect:/admin/file-manager/home";
    }

    @PostMapping("/edit-img")
    public String editImg(@RequestParam("imgName") String imgName,
                          @RequestParam("imgId") Long imgId,
                          @RequestParam("imgPath") String imgPath) {
        storageService.editImg(imgName, imgId, imgPath);
        return "redirect:/admin/file-manager/home";
    }

    @PostMapping("/get-imgs-by-folder")
    public String getImgsByFolder(@RequestParam("path") String path, RedirectAttributes atts, Model model) {
        model.addAttribute("listImages", storageService.loadAllFromDbByFolder(path));
//        atts.addFlashAttribute("listImages", storageService.loadAllFromDbByFolder(path));
        return "admin/file_manager/file-manager";
//        return "redirect:/admin/file-manager/home";
    }

    @PostMapping("/delete-img")
    public String editImg(@RequestParam("imgId") Long imgId,
                          @RequestParam("imgPath") String imgPath) {
        storageService.deleteImg(imgId, imgPath);
        return "redirect:/admin/file-manager/home";
    }

    @PostMapping("/move-img")
    public String moveImg(@RequestParam("movePath") String movePath,
                          @RequestParam("imgId") Long imgId,
                          @RequestParam("imgPath") String imgPath) {
        storageService.moveImg(imgId, imgPath, movePath);
        return "redirect:/admin/file-manager/home";
    }
}

package com.webecommerce.springboot.controller.admin;

import com.webecommerce.springboot.dto.AttributeAndValueDTO;
import com.webecommerce.springboot.dto.AttributeAndValueFilterDTO;
import com.webecommerce.springboot.dto.AttributeDTO;
import com.webecommerce.springboot.dto.AttributeValueDTO;
import com.webecommerce.springboot.service.AttributeService;
import com.webecommerce.springboot.service.AttributeValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/attribute")
public class AttributeController {

    @Autowired
    AttributeService attributeService;

    @Autowired
    AttributeValueService attributeValueService;

    @GetMapping("")
    public String indexPage(Model model) {
        model.addAttribute("listAttrs", attributeService.findAll());
        return "admin/attribute/index";
    }

    @GetMapping("/add-attr")
    public String addAttrPage(Model model) {
        model.addAttribute("attribute", new AttributeDTO());
        return "admin/attribute/add-attr";
    }

    @PostMapping("/add-attr")
    public String addAttrPage(AttributeDTO attributeDTO, RedirectAttributes atts) {
        attributeService.save(attributeDTO);
        atts.addFlashAttribute("message", "Thêm mới thuộc tính thành công");
        return "redirect:/admin/attribute";
    }

    @PostMapping("/add-attr-and-value")
    @ResponseBody
    public AttributeAndValueDTO addAttrAndValue(@RequestParam("attr") String attr, @RequestParam("value") String value) {
        return attributeService.addAttrAndValue(attr, value);
    }

    @GetMapping("/add-value")
    public String addValuePage(Model model) {
        model.addAttribute("listAttrs", attributeService.findAll());
        model.addAttribute("value", new AttributeValueDTO());
        return "admin/attribute/add-value";
    }

    @PostMapping("/add-value")
    public String addValue(@RequestParam("attrId") Long attrId, @RequestParam("value") String value, RedirectAttributes atts) {
        attributeValueService.save(attrId, value);
        atts.addFlashAttribute("message", "Thêm mới thuộc tính thành công");
        return "redirect:/admin/attribute";
    }

    @GetMapping("/edit-attr/{id}")
    public String editAttrPage(@PathVariable Long id, Model model) {
        model.addAttribute("attr", attributeService.findById(id));
        return "admin/attribute/edit-attr";
    }

    @PostMapping("/update")
    public String editAttr(AttributeDTO attr, RedirectAttributes atts) {
        attributeService.save(attr);
        atts.addFlashAttribute("message", "Thêm mới thuộc tính thành công");
        return "redirect:/admin/attribute";
    }

    @GetMapping("/attribute-value/{attrId}")
    @ResponseBody
    public List<AttributeValueDTO> getAttrValueByAttrId(@PathVariable Long attrId) {
        return  attributeService.findAllAttrValueByAttrId(attrId);
    }

    @PostMapping("/add-value-to-attr/{attrId}")
    public String addValueToAttr(@PathVariable Long attrId, @RequestParam("value") String value) {
        attributeValueService.save(attrId, value);
        return "redirect:/admin/attribute/edit-attr/"+attrId;
    }

    @GetMapping("/edit-value/{id}")
    @ResponseBody
    public AttributeValueDTO editValue(@PathVariable Long id) {
        return attributeValueService.findById(id);
    }

    @PostMapping("/value/save/{attrId}")
    public String saveValue(@PathVariable Long attrId, AttributeValueDTO attributeValueDTO) {
        attributeValueService.update(attributeValueDTO);
        return "redirect:/admin/attribute/edit-attr/"+attrId;
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable Long id, RedirectAttributes atts) {
        attributeService.remove(id);
        atts.addFlashAttribute("message", "Xóa thuộc tính thành công");
        return "redirect:/admin/attribute";
    }

    @PostMapping("/get-all-by-type-product-id")
    @ResponseBody
    public List<AttributeAndValueFilterDTO> findAllByTypeProductId(@RequestParam Long typeProductId) {
        return attributeService.findAllByTypeProductId(typeProductId);
    }

}

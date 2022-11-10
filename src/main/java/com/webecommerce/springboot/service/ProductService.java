package com.webecommerce.springboot.service;

import com.webecommerce.springboot.dto.AttributeAndValueDTO;
import com.webecommerce.springboot.dto.MinAndMaxPriceDTO;
import com.webecommerce.springboot.dto.ProductDTO;
import com.webecommerce.springboot.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface ProductService {

    List<ProductDTO> findAllProducts();

    List<ProductDTO> findAllProductsByName(String name);

    Page<ProductDTO> findAllProducts(Pageable pageable);

    MinAndMaxPriceDTO getMinAndMaxPrice();

    Page<ProductDTO> findAllProductsByCate(Pageable pageable, String cateSlug);

    List<AttributeAndValueDTO> getAttrAndValueById(Long productId);

//    @PreAuthorize("hasPermission('ProductEntity', 'EDIT')")
    ProductDTO save(ProductDTO productDTO);

    ProductDTO findById(Long id);

    ProductEntity findEntityById(Long id);

    Page<ProductDTO> search(Specification<ProductEntity> spec, Pageable pageable, String cateSlug);

    void remove(Long id);
}

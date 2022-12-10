package com.webecommerce.springboot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.webecommerce.springboot.dto.AttributeAndValueDTO;
import com.webecommerce.springboot.dto.MinAndMaxPriceDTO;
import com.webecommerce.springboot.dto.ProductDTO;
import com.webecommerce.springboot.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProductService {

    List<ProductDTO> findAllProducts();

    List<ProductDTO> findAllProductsByName(String name);

    Page<ProductDTO> findAllProducts(Pageable pageable);

    MinAndMaxPriceDTO getMinAndMaxPrice();

    Page<ProductDTO> findAllProductsByCate(Pageable pageable, String cateSlug);

    List<AttributeAndValueDTO> getAttrAndValueById(String productId);

//    @PreAuthorize("hasPermission('ProductEntity', 'EDIT')")
    ProductDTO save(ProductDTO productDTO) throws JsonProcessingException;

    ProductDTO findById(String id) throws JsonProcessingException;

    ProductEntity findEntityById(String id);

    Page<ProductDTO> filterProduct(Specification<ProductEntity> spec, Pageable pageable, String cateSlug);

    Page<ProductDTO> search(Map<String, String> params, Optional<Integer> page,
                            Optional<Integer> limit, Optional<String> sort, String path);

    void remove(String id);

    List<ProductDTO> findAllProductsByIds(List<String> ids);

    List<ProductDTO> findAllBySlugSeo(String slug);
}

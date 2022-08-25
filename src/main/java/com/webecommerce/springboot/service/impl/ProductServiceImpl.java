package com.webecommerce.springboot.service.impl;

import com.webecommerce.springboot.dto.*;
import com.webecommerce.springboot.entity.AttributeValueEntity;
import com.webecommerce.springboot.entity.CategoryEntity;
import com.webecommerce.springboot.entity.ImageEntity;
import com.webecommerce.springboot.entity.ProductEntity;
import com.webecommerce.springboot.repository.ProductRepository;
import com.webecommerce.springboot.service.AttributeValueService;
import com.webecommerce.springboot.service.CategoryService;
import com.webecommerce.springboot.service.ProductService;
import com.webecommerce.springboot.service.StorageService;
import com.webecommerce.springboot.specification.ProductSearchCriteria;
import com.webecommerce.springboot.specification.ProductSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    AttributeValueService attributeValueService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    StorageService storageService;

    @Autowired
    ModelMapper mapper;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<ProductDTO> findAllProducts() {
        return productRepository.findAll().stream().map(p -> {
            List<AttributeAndValueDTO> attributeAndValueDTOS = getAttrAndValueById(p.getId());
            ProductDTO productDTO = mapper.map(p, ProductDTO.class);
            productDTO.setAttributes(attributeAndValueDTOS);
            return productDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> findAllProductsByName(String name) {
        return productRepository.findAllByNameContaining(name).stream().map(p -> {
            List<AttributeAndValueDTO> attributeAndValueDTOS = getAttrAndValueById(p.getId());
            ProductDTO productDTO = mapper.map(p, ProductDTO.class);
            productDTO.setAttributes(attributeAndValueDTOS);
            return productDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public Page<ProductDTO> findAllProducts(Pageable pageable) {
        return convertListToPage(productRepository.findAll().stream().map(p -> {
            List<AttributeAndValueDTO> attributeAndValueDTOS = getAttrAndValueById(p.getId());
            ProductDTO productDTO = mapper.map(p, ProductDTO.class);
            productDTO.setAttributes(attributeAndValueDTOS);
            return productDTO;
        }).collect(Collectors.toList()), pageable);
    }

    @Override
    public MinAndMaxPriceDTO getMinAndMaxPrice() {
        return productRepository.getMinAndMaxPrice();
    }

    @Override
    public Page<ProductDTO> findAllProductsByCate(Pageable pageable, String cateSlug) {
        return convertListToPage(productRepository.findAll().stream()
                .filter(p -> existsProductByCateSlug(p.getCategories(), cateSlug))
                .map(p -> {
                    List<AttributeAndValueDTO> attributeAndValueDTOS = getAttrAndValueById(p.getId());
                    ProductDTO productDTO = mapper.map(p, ProductDTO.class);
                    productDTO.setAttributes(attributeAndValueDTOS);
                    return productDTO;
                }).collect(Collectors.toList()), pageable);
    }

    @Override
    public List<AttributeAndValueDTO> getAttrAndValueById(Long productId) {
        if(productRepository.getAttrAndValueByProductId(productId).size() > 0) {
            return productRepository.getAttrAndValueByProductId(productId).stream()
                    .map(o -> new AttributeAndValueDTO(new AttributeDTO((Long) o[0], (String) o[1]),
                            new AttributeValueDTO((Long) o[2], (String) o[3]))
                    ).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public ProductDTO save(ProductDTO productDTO) {
        ProductEntity newProduct = mapper.map(productDTO, ProductEntity.class);
        if (productDTO.getAttributes() != null) {
            List<AttributeValueEntity> attributeValueEntities =
                    productDTO.getAttributes().stream()
                            .filter(a -> Objects.nonNull(a.getAttrValue()))
                            .map(a -> attributeValueService.findEntityById(a.getAttrValue().getId()))
                            .filter(v -> Objects.nonNull(v))
                            .collect(Collectors.toList());
            newProduct.setAttributeValueEntities(attributeValueEntities);
        }
        List<CategoryEntity> categoryEntities =
                productDTO.getCategories().stream().map(c -> categoryService.findEntityById(c.getId()))
                        .collect(Collectors.toList());
        newProduct.setCategories(categoryEntities);
        if (productDTO.getImages() != null) {
            List<ImageEntity> imageEntities =
                    productDTO.getImages().stream().map(i -> storageService.findByIdFromDb(i.getId()))
                            .collect(Collectors.toList());
            newProduct.setImages(imageEntities);
        }
        productRepository.save(newProduct);
        return productDTO;
    }

    @Override
    public ProductDTO findById(Long id) {
        Optional<ProductEntity> foundProduct = productRepository.findById(id);
        if (!foundProduct.isPresent()) {
            throw new RuntimeException("");
        }
        ProductDTO product = mapper.map(foundProduct.get(), ProductDTO.class);
        product.setAttributes(getAttrAndValueById(id));
        return product;
    }

    @Override
    public ProductEntity findEntityById(Long id) {
        Optional<ProductEntity> foundProduct = productRepository.findById(id);
        return foundProduct.orElseThrow(() -> new IllegalArgumentException("Not found"));
    }

    public boolean existsProductByCateSlug(List<CategoryEntity> listCate, String cateSlug) {
        return listCate.stream().anyMatch(c -> c.getSlug().equals(cateSlug));
    }

    @Override
    public Page<ProductDTO> search(Specification<ProductEntity> spec, Pageable pageable, String cateSlug) {
        return convertListToPage(productRepository.findAll(spec).stream()
                .filter(p -> existsProductByCateSlug(p.getCategories(), cateSlug))
                .map(p -> {
                    List<AttributeAndValueDTO> attributeAndValueDTOS = getAttrAndValueById(p.getId());
                    ProductDTO productDTO = mapper.map(p, ProductDTO.class);
                    productDTO.setAttributes(attributeAndValueDTOS);
                    return productDTO;
                }).collect(Collectors.toList()), pageable);
    }

    @Override
    public void remove(Long id) {
        productRepository.deleteById(id);
    }

    public <T> Page<T> convertListToPage(List<T> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = ((start + pageable.getPageSize()) > list.size() ? list.size()
                : (start + pageable.getPageSize()));
        return new PageImpl<T>(list.subList(start, end), pageable, list.size());
    }
}

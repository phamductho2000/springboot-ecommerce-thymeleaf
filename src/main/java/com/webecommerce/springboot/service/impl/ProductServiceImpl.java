package com.webecommerce.springboot.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.webecommerce.springboot.specification.ProductSpecificationsBuilder;
import com.webecommerce.springboot.util.Util;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
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
        return productRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt")).stream().map(p -> {
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
        return convertListToPage(productRepository.findAll(pageable).stream()
                .filter(p -> existsProductByCateSlug(p.getCategories(), cateSlug))
                .map(p -> {
                    List<AttributeAndValueDTO> attributeAndValueDTOS = getAttrAndValueById(p.getId());
                    ProductDTO productDTO = mapper.map(p, ProductDTO.class);
                    productDTO.setAttributes(attributeAndValueDTOS);
                    return productDTO;
                }).collect(Collectors.toList()), pageable);
    }

    @Override
    public List<AttributeAndValueDTO> getAttrAndValueById(String productId) {
        if (productRepository.getAttrAndValueByProductId(productId).size() > 0) {
            return productRepository.getAttrAndValueByProductId(productId).stream()
                    .map(o -> new AttributeAndValueDTO(new AttributeDTO((Long) o[0], (String) o[1], (String) o[2], null),
                            new AttributeValueDTO((Long) o[3], (String) o[4]))
                    ).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public ProductDTO save(ProductDTO productDTO) throws JsonProcessingException {
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
        ProductEntity resultSave = productRepository.save(newProduct);
        if (!productDTO.getGroupProducts().isEmpty()) {
            ObjectMapper convertJson = new ObjectMapper();
            List<String> groupIds = productDTO.getGroupProducts().stream().map(pro -> pro.getId()).collect(Collectors.toList());
            groupIds.add(resultSave.getId());
            resultSave.setGroupId(convertJson.writeValueAsString(groupIds));
            productRepository.save(resultSave);
            updateGroupId(groupIds);
        }
        return productDTO;
    }

    @Override
    public ProductDTO findById(String id) throws JsonProcessingException {
        Optional<ProductEntity> foundProduct = productRepository.findById(id);
        if (!foundProduct.isPresent()) {
            return null;
        }
        ProductDTO product = mapper.map(foundProduct.get(), ProductDTO.class);
        product.setAttributes(getAttrAndValueById(id));
        if (foundProduct.get().getGroupId() != null && !foundProduct.get().getGroupId().isEmpty()) {
            ObjectMapper convertJson = new ObjectMapper();
            List<String> groupIds = convertJson.readValue(foundProduct.get().getGroupId(), new TypeReference<List<String>>() {
            });
            product.setGroupProducts(findAllProductsByIds(groupIds));
        }
        return product;
    }

    @Override
    public ProductEntity findEntityById(String id) {
        Optional<ProductEntity> foundProduct = productRepository.findById(id);
        return foundProduct.orElseThrow(() -> new IllegalArgumentException("Not found"));
    }

    public boolean existsProductByCateSlug(List<CategoryEntity> listCate, String cateSlug) {
        return listCate.stream().anyMatch(c -> c.getSlug().equals(cateSlug));
    }

    @Override
    public Page<ProductDTO> filterProduct(Specification<ProductEntity> spec, Pageable pageable, String cateSlug) {
        return convertListToPage(productRepository.findAll(spec, pageable).stream()
                .filter(p -> existsProductByCateSlug(p.getCategories(), cateSlug))
                .map(p -> {
                    List<AttributeAndValueDTO> attributeAndValueDTOS = getAttrAndValueById(p.getId());
                    ProductDTO productDTO = mapper.map(p, ProductDTO.class);
                    productDTO.setAttributes(attributeAndValueDTOS);
                    return productDTO;
                }).collect(Collectors.toList()), pageable);
    }

    @Override
    public Page<ProductDTO> search(Map<String, String> params, Optional<Integer> page,
                                   Optional<Integer> limit, Optional<String> sort, String path) {
        Map<String, String> searchParams = Util.getSearchParams(params);
        String fieldSort = "name";
        String valueSort = "ASC";
        if (sort.isPresent()) {
            fieldSort = sort.get().split("-")[0];
            valueSort = sort.get().split("-")[1].toUpperCase();
        }
        if (!searchParams.isEmpty()) {
            ProductSpecificationsBuilder builder = new ProductSpecificationsBuilder();
            searchParams.entrySet().stream()
                    .forEach(p -> builder.with(p.getKey(), "=", p.getValue()));
            Specification<ProductEntity> spec = builder.build();
            return filterProduct(spec, PageRequest.of(
                    page.orElse(1) - 1,
                    limit.orElse(9),
                    Sort.by(Sort.Direction.valueOf(valueSort), fieldSort)), path
            );
        } else {
            return findAllProductsByCate(
                    PageRequest.of(page.orElse(1) - 1,
                            limit.orElse(9),
                            Sort.by(Sort.Direction.valueOf(valueSort), fieldSort)),
                    path
            );
        }
    }

    @Override
    public void remove(String id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<ProductDTO> findAllProductsByIds(List<String> ids) {
        return productRepository.findAllById(ids).stream().map(p -> {
            List<AttributeAndValueDTO> attributeAndValueDTOS = getAttrAndValueById(p.getId());
            ProductDTO productDTO = mapper.map(p, ProductDTO.class);
            productDTO.setAttributes(attributeAndValueDTOS);
            return productDTO;
        }).collect(Collectors.toList());
    }

    public void updateGroupId(List<String> groupIds) {
        List<ProductEntity> products = productRepository.findAllById(groupIds);
        products.stream().forEach(pro -> {
            ObjectMapper convertJson = new ObjectMapper();
            try {
                pro.setGroupId(convertJson.writeValueAsString(groupIds));
                productRepository.save(pro);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public List<ProductDTO> findAllBySlugSeo(String slug) {
       return productRepository.findAllBySlugSeo(slug)
               .stream()
               .map(p -> mapper.map(p, ProductDTO.class))
               .collect(Collectors.toList());
    }

    public <T> Page<T> convertListToPage(List<T> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = ((start + pageable.getPageSize()) > list.size() ? list.size()
                : (start + pageable.getPageSize()));
        return new PageImpl<T>(list.subList(start, end), pageable, list.size());
    }
}

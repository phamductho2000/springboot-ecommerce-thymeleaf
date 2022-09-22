package com.webecommerce.springboot.repository;

import com.webecommerce.springboot.dto.MinAndMaxPriceDTO;
import com.webecommerce.springboot.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, Long>, JpaSpecificationExecutor<ProductEntity> {

    @Query("SELECT ae.id, ae.name, ae.code, ave.id, ave.value " +
            "FROM AttributeValueEntity ave " +
            "INNER JOIN ave.attributeEntity ae " +
            "WHERE ae.deleted = false AND ave.id IN " +
            "(SELECT ave.id FROM ave.productEntities avpe WHERE avpe.id = ?1)")
    List<Object[]> getAttrAndValueByProductId(Long productId);

    @Query("SELECT new com.webecommerce.springboot.dto.MinAndMaxPriceDTO(min(p.price), max(p.price)) FROM ProductEntity p")
    MinAndMaxPriceDTO getMinAndMaxPrice();

    List<ProductEntity> findAllByNameContaining(String name);
}

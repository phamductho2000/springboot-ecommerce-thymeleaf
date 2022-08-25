package com.webecommerce.springboot.repository;

import com.webecommerce.springboot.dto.AttributeValueDTO;
import com.webecommerce.springboot.entity.AttributeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface AttributeRepository extends JpaRepository<AttributeEntity, Long> {

    @Query("SELECT new com.webecommerce.springboot.dto.AttributeValueDTO(av.id, av.value) FROM AttributeEntity a JOIN a.attributeValueEntities av WHERE a.id = ?1")
    List<AttributeValueDTO> findAllAttrValueByAttrId(Long attrId);

    @Query("SELECT ae.id, ae.name, ave.id, ave.value " +
            "FROM AttributeValueEntity ave " +
            "INNER JOIN ave.attributeEntity ae " +
            "WHERE ae.deleted = false AND ave.id IN " +
            "(SELECT ave.id FROM ave.productEntities avpe " +
            "INNER JOIN avpe.categories ce " +
            "WHERE ce.slug LIKE %?1%)")
    List<Object[]> getAttrAndValueByCate(String cate);

    List<AttributeEntity> findAllByTypeProductEntityId(Long typeProductId);
}

package com.webecommerce.springboot.repository;

import com.webecommerce.springboot.entity.AttributeValueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttributeValueRepository extends JpaRepository<AttributeValueEntity, Long> {
}

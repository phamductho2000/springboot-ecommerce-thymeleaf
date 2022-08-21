package com.webecommerce.springboot.repository;

import com.webecommerce.springboot.entity.DetailOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailOrderRepository extends JpaRepository<DetailOrderEntity, Long> {
}

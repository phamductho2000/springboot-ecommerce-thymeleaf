package com.webecommerce.springboot.repository;

import com.webecommerce.springboot.entity.DeliveryAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryAddressRepository extends JpaRepository<DeliveryAddressEntity, Long> {

    List<DeliveryAddressEntity> findAllByUserEntity_Email(String email);

    List<DeliveryAddressEntity> findAllByUserEntity_Id(Long id);

}

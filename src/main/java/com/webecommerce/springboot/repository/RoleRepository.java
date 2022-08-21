package com.webecommerce.springboot.repository;

import com.webecommerce.springboot.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    RoleEntity findByCode(String code);

    List<RoleEntity> findAllByUsers_Username(String username);

    List<RoleEntity> findAllByCode(String code);

}

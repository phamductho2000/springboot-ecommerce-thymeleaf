package com.webecommerce.springboot.repository;

import com.webecommerce.springboot.entity.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {

    List<PermissionEntity> findByRoles_Id(long roleId);

    List<PermissionEntity> findAllByTable(String table);

    List<PermissionEntity> findAllByGeneralName(String genName);
}

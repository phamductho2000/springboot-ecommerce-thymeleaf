package com.webecommerce.springboot.repository;

import com.webecommerce.springboot.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByUsernameAndStatus(String username, Boolean status);

    UserEntity findByEmail(String email);

    @Query("SELECT u FROM UserEntity u JOIN u.roles r WHERE r.code <> 'USER'")
    List<UserEntity> findAllAccount();

    @Query("SELECT u FROM UserEntity u JOIN u.roles r WHERE r.code = 'USER'")
    List<UserEntity> findAllCustomer();
}

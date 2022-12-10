package com.webecommerce.springboot.repository;

import com.webecommerce.springboot.entity.SavedId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SavedIdRepository extends JpaRepository<SavedId, Long> {

    @Query("SELECT s.latestId FROM SavedId s WHERE s.type = ?1")
    Optional<String> getLatestIdByType(String type);

    Optional<SavedId> findByType(String type);
}

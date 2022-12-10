package com.webecommerce.springboot.repository;

import com.webecommerce.springboot.dto.RevenuesAndBenefitByYearDTO;
import com.webecommerce.springboot.dto.RevenuesAndBenefitDTO;
import com.webecommerce.springboot.dto.TopSellingProductDTO;
import com.webecommerce.springboot.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, String> {

    @Query("SELECT new com.webecommerce.springboot.dto.RevenuesAndBenefitDTO(SUM(do.quantity * do.price), SUM(do.quantity * do.price) - SUM(do.quantity * p.originalPrice))" +
            "FROM OrderEntity o JOIN o.detailOrders do JOIN do.product p " +
            "WHERE o.status = 5")
    RevenuesAndBenefitDTO getRevenueAndBenefit();

    @Query("SELECT new com.webecommerce.springboot.dto.TopSellingProductDTO(" +
            "p.id, p.name, SUM(do.quantity), do.price, SUM(do.quantity * do.price))" +
            "FROM DetailOrderEntity do JOIN do.product p GROUP BY p.id ")
    List<TopSellingProductDTO> getTopSellingProduct();

    @Query("SELECT new com.webecommerce.springboot.dto.RevenuesAndBenefitByYearDTO(" +
            "YEAR(o.createdAt), " +
            "MONTH(o.createdAt), " +
            "SUM(do.quantity * do.price), " +
            "SUM(do.quantity * do.price) - SUM(do.quantity * p.originalPrice)) " +
            "FROM OrderEntity o JOIN o.detailOrders do JOIN do.product p " +
            "WHERE o.status = 5 AND YEAR(o.createdAt) = :year " +
            "GROUP BY YEAR(o.createdAt), MONTH(o.createdAt)")
    List<RevenuesAndBenefitByYearDTO> getRevenueAndBenefitByYear(@Param("year") int year);

    List<OrderEntity> findAllByUser_Email(String email);
}

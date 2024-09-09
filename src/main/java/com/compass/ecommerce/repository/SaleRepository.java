package com.compass.ecommerce.repository;

import com.compass.ecommerce.model.Sale;
import com.compass.ecommerce.model.enums.SaleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query("SELECT s FROM Sale s JOIN s.itemSales i WHERE i.product.id = :productId AND s.status = :status")
    List<Sale> findAllByProductIdAndStatus(Long productId, SaleStatus status);

    @Query("SELECT s FROM Sale s WHERE s.date BETWEEN :startDate AND :endDate")
    List<Sale> findSalesByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

}

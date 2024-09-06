package com.compass.ecommerce.repository;

import com.compass.ecommerce.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    Optional<Sale> findByUserIdAndId(Long saleId, Long userId);
}

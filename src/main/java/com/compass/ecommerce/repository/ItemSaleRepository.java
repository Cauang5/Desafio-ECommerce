package com.compass.ecommerce.repository;

import com.compass.ecommerce.model.ItemSale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemSaleRepository extends JpaRepository<ItemSale, Long> {

    @Modifying
    @Query("DELETE FROM ItemSale i WHERE i.product.id = :productId")
    void deleteByProductId(@Param("productId") Long productId);
}

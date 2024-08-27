package com.compass.ecommerce.DTO;

public record ItemSaleDTO(Long productId,
                          String productName,
                          String productDescription,
                          Integer quantity) {
}

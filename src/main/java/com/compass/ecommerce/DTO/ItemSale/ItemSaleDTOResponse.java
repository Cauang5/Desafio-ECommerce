package com.compass.ecommerce.DTO.ItemSale;

public record ItemSaleDTOResponse(Long productId,
                                  String productName,
                                  String description,
                                  Integer quantity) {
}

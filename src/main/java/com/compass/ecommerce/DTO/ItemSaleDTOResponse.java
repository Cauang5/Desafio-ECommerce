package com.compass.ecommerce.DTO;

public record ItemSaleDTOResponse(Long productId,
                                  String productName,
                                  String description,
                                  Integer quantity) {
}

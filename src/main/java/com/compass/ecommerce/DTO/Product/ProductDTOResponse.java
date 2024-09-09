package com.compass.ecommerce.DTO.Product;

import java.math.BigDecimal;

public record ProductDTOResponse(Long id,
                                 String name,
                                 String descrpition,
                                 BigDecimal price,
                                 Integer quantity,
                                 Integer stock) {
}

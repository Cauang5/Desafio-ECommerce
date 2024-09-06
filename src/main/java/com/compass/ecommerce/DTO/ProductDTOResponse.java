package com.compass.ecommerce.DTO;

import java.math.BigDecimal;

public record ProductDTOResponse(Long id,
                                 String name,
                                 String descrpition,
                                 BigDecimal price,
                                 Integer quantity) {
}

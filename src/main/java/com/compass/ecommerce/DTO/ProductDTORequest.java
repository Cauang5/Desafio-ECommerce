package com.compass.ecommerce.DTO;

import java.math.BigDecimal;

public record ProductDTORequest(String name,
                                String description,
                                BigDecimal price,
                                Integer quantity) {
}

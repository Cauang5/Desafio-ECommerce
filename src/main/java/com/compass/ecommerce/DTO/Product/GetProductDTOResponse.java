package com.compass.ecommerce.DTO.Product;

import java.math.BigDecimal;

public record GetProductDTOResponse(Long id,
                                    String name,
                                    String descrpition,
                                    BigDecimal price,
                                    Integer stock) {
}

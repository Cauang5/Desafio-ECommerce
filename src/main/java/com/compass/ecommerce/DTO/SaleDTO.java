package com.compass.ecommerce.DTO;

import java.math.BigDecimal;
import java.util.List;

public record SaleDTO(Long id,
                      BigDecimal total,
                      List<ItemSaleDTO> itens) {
}

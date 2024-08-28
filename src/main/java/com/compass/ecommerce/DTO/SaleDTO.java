package com.compass.ecommerce.DTO;

import java.math.BigDecimal;
import java.util.List;

public record SaleDTO(BigDecimal total,
                      List<ItemSaleDTO> itens) {
}

package com.compass.ecommerce.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record SaleDTOResponse(Long id,
                              String userName,
                              LocalDateTime dateTime,
                              BigDecimal total,
                              List<ItemSaleDTOResponse> itens) {
}

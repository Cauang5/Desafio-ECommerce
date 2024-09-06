package com.compass.ecommerce.DTO;

import com.compass.ecommerce.model.enums.SaleStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record SaleDTOResponse(Long id,
                              String userName,

                              @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC" )
                              LocalDateTime dateTime,

                              SaleStatus status,
                              BigDecimal total,
                              List<ItemSaleDTOResponse> itens) {
}

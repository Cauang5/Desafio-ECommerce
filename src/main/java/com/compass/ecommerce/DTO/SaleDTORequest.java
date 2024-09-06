package com.compass.ecommerce.DTO;

import com.compass.ecommerce.model.enums.SaleStatus;

import java.util.List;

public record SaleDTORequest(Long userId,
                             SaleStatus status,
                             List<ItemSaleDTORequest> items) {
}

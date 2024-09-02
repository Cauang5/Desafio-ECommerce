package com.compass.ecommerce.DTO;

import java.util.List;

public record SaleDTORequest(Long userId,
                             List<ItemSaleDTORequest> items) {
}

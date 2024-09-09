package com.compass.ecommerce.DTO.ItemSale;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ItemSaleDTORequest(Long productId,

                                 @NotNull(message = "A quantidade é obrigatória.")
                                 @Min(value = 1, message = "A quantidade deve ser pelo menos 1.")
                                 Integer stock) {
}

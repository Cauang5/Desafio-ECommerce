package com.compass.ecommerce.DTO.Product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateStockDTORequest(
        @Min(value = 1, message = "A quantidade de produtos deve ser maior que zero.")
        @NotNull
        Integer quantity) {
}

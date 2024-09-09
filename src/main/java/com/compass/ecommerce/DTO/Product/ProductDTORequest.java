package com.compass.ecommerce.DTO.Product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductDTORequest(
        @Size(min = 2, max = 100, message = "O nome do produto deve conter entre 2 e 100 caracteres")
        String name,

        @Size(min = 5, max = 255, message = "A descrição do produtodeve ter entre 5 e 255 caracteres")
        String description,

        @NotNull(message = "O preço do produto não pode ser nulo ")
        @Positive(message = "O Preço do produto deverá ser positivo")
        BigDecimal price,

        @NotNull(message = " A quantidade minima do produto não pode ser nulo ")
        @Min(value = 1, message = "A quantidade mínima do produto deve ser 1")
        Integer quantity,

        @Min(value = 0, message = "O estoque do produto não pode ser negativo")
        Integer stock) {
}

package com.compass.ecommerce.DTO.Sale;

import com.compass.ecommerce.DTO.ItemSale.ItemSaleDTORequest;
import com.compass.ecommerce.model.enums.SaleStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record SaleDTORequest(
        @NotNull(message = "O ID do usuário é obrigatório.")
        Long userId,

        SaleStatus status,

        @NotNull(message = "A lista de itens não pode ser nula.")
        @NotEmpty(message = "A lista de itens não pode estar vazia.")
        @Size(min = 1, message = "A venda deve conter pelo menos um item.")
        List<ItemSaleDTORequest> items) {
}

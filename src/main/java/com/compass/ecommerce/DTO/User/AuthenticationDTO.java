package com.compass.ecommerce.DTO.User;

import jakarta.validation.constraints.NotBlank;

public record AuthenticationDTO(
        @NotBlank(message = "Login não pode ser vazio")
        String login,

        @NotBlank(message = "A Senha não pode ser vazio")
        String password) {
}

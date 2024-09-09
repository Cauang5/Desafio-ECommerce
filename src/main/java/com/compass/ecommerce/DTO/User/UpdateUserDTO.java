package com.compass.ecommerce.DTO.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateUserDTO(
        @Size(min =  2, max = 100, message = "O nome deve conter entre 2 e 100 caracteres")
        String name,


        @Size(min =  2, max = 100, message = "O login deve conter entre 2 e 100 caracteres")
        String login,

        @NotBlank(message = "O email do usuário não pode ser vazio")
        @Email(message = "O email tem que ser válido")
        String email) {
}

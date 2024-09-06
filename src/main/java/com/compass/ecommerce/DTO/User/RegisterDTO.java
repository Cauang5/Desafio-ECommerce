package com.compass.ecommerce.DTO.User;

import com.compass.ecommerce.model.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record RegisterDTO(

        @Size(min = 2, max = 100, message = "O nome deve conter entre 2 e 100 caracteres")
        String name,

        @Email(message = "O email tem que ser válido")
        String email,

        @Size(min =  2, max = 100, message = "O login deve conter entre 2 e 100 caracteres")
        String login,

        @Size(min = 8, message = "A senha tem que conter pelo menos 8 caracteres")
        String password,

        UserRole role) {
}

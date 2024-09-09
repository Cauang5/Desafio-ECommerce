package com.compass.ecommerce.DTO.User;

import jakarta.validation.constraints.Size;

public record UpdatePasswordDTO(String oldPassword,

                                @Size(min = 8, message = "A senha tem que conter pelo menos 8 caracteres")
                                String newPassword,

                                String confirmPassword) {
}

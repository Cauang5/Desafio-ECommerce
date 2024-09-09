package com.compass.ecommerce.DTO.User;

import jakarta.validation.constraints.Size;

public record ResetPasswordDTO(
        @Size(min = 2, max = 100, message = "O login deve conter entre 2 e 100 caracteres")
        String login,

        @Size(min = 8, message = "A senha tem que conter mais do que 8 caracteres")
        String newPassword,

        String confirmPassword) {
}

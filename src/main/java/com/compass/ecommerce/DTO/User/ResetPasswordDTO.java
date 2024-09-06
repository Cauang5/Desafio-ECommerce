package com.compass.ecommerce.DTO.User;

public record ResetPasswordDTO(
        String login,
        String newPassword,
        String confirmPassword) {
}

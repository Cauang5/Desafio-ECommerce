package com.compass.ecommerce.DTO.User;

public record UpdatePasswordDTO(String oldPassword,
                                String newPassword,
                                String confirmPassword) {
}

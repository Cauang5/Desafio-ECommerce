package com.compass.ecommerce.model.enums;

public enum UserRole {

    ADMIN("admin"),

    USER("user");

    private String role;

    UserRole(String role) {
        this.role = role;
    }

    public String role() {
        return role;
    }
}

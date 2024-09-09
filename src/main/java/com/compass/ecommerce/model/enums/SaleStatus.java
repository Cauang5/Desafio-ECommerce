package com.compass.ecommerce.model.enums;

public enum SaleStatus {
    PROCESSING("processing"),
    CANCELED("canceled"),
    FINISHED("finished");

    private String status;

    SaleStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    // Método para converter uma string para o enum correspondente
    public static SaleStatus fromString(String status) {
        for (SaleStatus saleStatus : SaleStatus.values()) {
            if (saleStatus.status.equalsIgnoreCase(status)) {
                return saleStatus;
            }
        }
        throw new IllegalArgumentException("Estado de venda inválido: " + status);
    }

    @Override
    public String toString() {
        return this.status;
    }
}

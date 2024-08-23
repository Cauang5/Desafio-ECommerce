package com.compass.ecommerce.Exception;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErroResponse {

    private int status;
    private String message;
    private LocalDateTime timestamp;

    public ErroResponse(int status, String message, LocalDateTime timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }
}

package com.ecom.product.dto;

import java.time.LocalDateTime;

public class CustomResponse {
    private Boolean success;
    private String message;

    private LocalDateTime timestamp;

    public CustomResponse(Boolean status, String message) {
        this.success = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public Boolean getStatus() {
        return success;
    }

    public void setStatus(Boolean status) {
        this.success = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

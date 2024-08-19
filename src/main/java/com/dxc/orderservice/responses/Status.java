package com.dxc.orderservice.responses;

import com.dxc.orderservice.util.exceptions.ServiceException;

public final class Status {
    private int code;
    private String message;
    private String type;

    public Status(final int code, final String message, final String type) {
        this.code = code;
        this.message = message;
        this.type = type;
    }

    // If success, you can just pass the message
    public Status(final String message) {
        this.code = 0;
        this.message = message;
        this.type = "success";
    }

    // If failed with custom defined exception, you can just pass the exception
    public Status(final ServiceException ex) {
        this.code = ex.getErrorCode();
        this.message = ex.getMessage();
        this.type = "error";
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getType() {
        return type;
    }
}

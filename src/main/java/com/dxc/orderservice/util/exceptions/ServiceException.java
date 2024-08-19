package com.dxc.orderservice.util.exceptions;

public abstract class ServiceException extends RuntimeException {
    private final int errorCode;

    public ServiceException(final int errorCode, final String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public final int getErrorCode() {
        return errorCode;
    }
}

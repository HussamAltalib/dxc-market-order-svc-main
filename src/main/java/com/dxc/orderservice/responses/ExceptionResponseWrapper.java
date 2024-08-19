package com.dxc.orderservice.responses;

public final class ExceptionResponseWrapper {
    private Status status;


    public ExceptionResponseWrapper(final Status status) {
        this.status = status;

    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(final Status status) {
        this.status = status;
    }

}

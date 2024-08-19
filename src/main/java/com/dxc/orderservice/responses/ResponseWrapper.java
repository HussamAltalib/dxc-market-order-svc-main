package com.dxc.orderservice.responses;

public final class ResponseWrapper<T> {
    private Status status;
    private T data;

    public ResponseWrapper(final Status status, final T data) {
        this.status = status;
        this.data = data;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(final Status status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(final T data) {
        this.data = data;
    }
}

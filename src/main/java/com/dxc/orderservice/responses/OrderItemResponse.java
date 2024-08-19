package com.dxc.orderservice.responses;

public final class OrderItemResponse {
    private String productName;
    private int quantity;

    public String getProductName() {
        return productName;
    }

    public void setProductName(final String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(final int quantity) {
        this.quantity = quantity;
    }


}

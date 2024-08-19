package com.dxc.orderservice.models;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public final class OrderItemCompositeKey implements Serializable {

    @Column(name = "order_id")
    private int orderId;

    @Column(name = "product_id")
    private int productId;

    public int getOrderId() {
        return orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setOrderId(final int orderId) {
        this.orderId = orderId;
    }

    public void setProductId(final int productId) {
        this.productId = productId;
    }
}

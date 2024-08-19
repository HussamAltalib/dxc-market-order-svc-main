package com.dxc.orderservice.models;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "order_items")
public final class OrderItem {

    @EmbeddedId
    private OrderItemCompositeKey id;


    @Column(name = "quantity", nullable = false)
    private int quantity;



    public OrderItemCompositeKey getId() {
        return id;
    }

    public void setId(final OrderItemCompositeKey id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(final int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "OrderItem{"
                + "id=" + id
                + ", quantity=" + quantity
                + '}';
    }
}

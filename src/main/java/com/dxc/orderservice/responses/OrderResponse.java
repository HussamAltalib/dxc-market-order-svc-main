package com.dxc.orderservice.responses;

import com.dxc.orderservice.models.Order;

import java.time.LocalDateTime;
import java.util.List;


public final class OrderResponse {
    private int id;
    private String customerName;
    private Order.OrderStatus status;
    private double totalAmount;
    private LocalDateTime createdAt;
    private List<OrderItemResponse> items;

    public OrderResponse() {

    }

    public OrderResponse(
            final int id,
            final String customerName,
            final Order.OrderStatus status,
            final double totalAmount,
            final LocalDateTime createdAt,
            final List<OrderItemResponse> items) {
        this.id = id;
        this.customerName = customerName;
        this.status = status;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
        this.items = items;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(final String customerName) {
        this.customerName = customerName;
    }

    public Order.OrderStatus getStatus() {
        return status;
    }

    public void setStatus(final Order.OrderStatus status) {
        this.status = status;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(final double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<OrderItemResponse> getItems() {
        return items;
    }

    public void setItems(final List<OrderItemResponse> items) {
        this.items = items;
    }


}

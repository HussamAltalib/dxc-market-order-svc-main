package com.dxc.orderservice.models;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public final class Order {
    public static final int PRECISION = 10;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int orderId;

    @Column(name = "customer_id")
    private int customerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition =
            "ENUM('PENDING', 'COMPLETED', 'CANCELLED') DEFAULT 'PENDING'")
    private OrderStatus status;

    @Column(name = "total_amount",
            precision = PRECISION,
            scale = 2,
            nullable = false)
    private double totalAmount;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;


    public Order() {

    }

    public Order(final int orderId, final int customerId, final OrderStatus status,
                 final double totalAmount, final LocalDateTime createdAt) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.status = status;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(final int orderId) {
        this.orderId = orderId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(final int customerId) {
        this.customerId = customerId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(final OrderStatus status) {
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

    public enum OrderStatus {
        PENDING, COMPLETED, CANCELLED
    }


    @Override
    public String toString() {
        return "Order{"
                + "id=" + orderId
                + ", customerId=" + customerId
                + ", status=" + status
                + ", totalAmount=" + totalAmount
                + ", createdAt=" + createdAt
                + '}';
    }
}

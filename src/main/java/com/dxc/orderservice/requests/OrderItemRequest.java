package com.dxc.orderservice.requests;

import javax.validation.constraints.*;
import java.util.Objects;

public final class OrderItemRequest {

    @NotNull(message = "Product ID is required")
    @Positive(message = "Product ID must be a positive number")
    private Integer productId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "The 'quantity' must be at least 1.")
    @Max(value = 100, message = "The 'quantity' must not exceed 100.")
    private Integer quantity;


    public Integer getProductId() {
        return productId;
    }

    public void setProductId(final Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(final int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "OrderItemRequest{"
                + "productId=" + productId
                + ", quantity=" + quantity
                + '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderItemRequest that = (OrderItemRequest) o;
        return Objects.equals(productId, that.productId)
                && Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, quantity);
    }
}

package com.dxc.orderservice.requests;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;


public final class OrderRequest {

    private int customerId;

    @Size(min = 1, message = "The 'items' field must include at least one item")
    @NotNull(message = "The 'items' field is missing")
    @Valid // Add this annotation to validate annotations in OrderItemRequest
    private List<OrderItemRequest> items;


    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(final int customerId) {
        this.customerId = customerId;
    }

    public List<OrderItemRequest> getItems() {
        return items;
    }

    public void setItems(final List<OrderItemRequest> items) {
        this.items = items;
    }

}

package com.dxc.orderservice.repositories;

import com.dxc.orderservice.models.OrderItem;
import com.dxc.orderservice.models.OrderItemCompositeKey;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderItemRepository
        extends CrudRepository<OrderItem, OrderItemCompositeKey> {
    List<OrderItem> findByIdOrderId(int orderId);
}

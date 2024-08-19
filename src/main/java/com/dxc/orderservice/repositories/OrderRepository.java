package com.dxc.orderservice.repositories;

import com.dxc.orderservice.models.Customer;
import com.dxc.orderservice.models.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<Order, Integer> {

    List<Order> findOrdersByCustomerId(int customerId);
    int getCustomerIdByOrderId(int orderId);
}

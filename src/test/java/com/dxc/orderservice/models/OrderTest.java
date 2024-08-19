package com.dxc.orderservice.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OrderTest {

    private Order order;

    @BeforeEach
    public void setUp() {
        order = new Order();
    }

    @Test
    public void testId() {
        int expectedId = 1;
        order.setOrderId(expectedId);
        assertEquals(expectedId, order.getOrderId());
    }

    @Test
    public void testCustomerId() {
        int expectedCustomerId = 101;
        order.setCustomerId(expectedCustomerId);
        assertEquals(expectedCustomerId, order.getCustomerId());
    }

    @Test
    public void testStatus() {
        Order.OrderStatus expectedStatus = Order.OrderStatus.PENDING;
        order.setStatus(expectedStatus);
        assertEquals(expectedStatus, order.getStatus());
    }

    @Test
    public void testTotalAmount() {
        double expectedTotalAmount = 250.75;
        order.setTotalAmount(expectedTotalAmount);
        assertEquals(expectedTotalAmount, order.getTotalAmount());
    }

    @Test
    public void testCreatedAt() {
        LocalDateTime expectedDate = LocalDateTime.now();
        order.setCreatedAt(expectedDate);
        assertEquals(expectedDate, order.getCreatedAt());
    }

    @Test
    public void testFullConstructor() {
        int expectedId = 2;
        int expectedCustomerId = 102;
        Order.OrderStatus expectedStatus = Order.OrderStatus.COMPLETED;
        double expectedTotalAmount = 500.75;
        LocalDateTime expectedCreatedAt = LocalDateTime.now();

        order = new Order(expectedId, expectedCustomerId, expectedStatus, expectedTotalAmount, expectedCreatedAt);
        assertEquals(expectedId, order.getOrderId());
        assertEquals(expectedCustomerId, order.getCustomerId());
        assertEquals(expectedStatus, order.getStatus());
        assertEquals(expectedTotalAmount, order.getTotalAmount());
        assertEquals(expectedCreatedAt, order.getCreatedAt());
    }

    @Test
    public void testToString() {
        order.setOrderId(1);
        order.setCustomerId(101);
        order.setStatus(Order.OrderStatus.PENDING);
        order.setTotalAmount(250.75);
        order.setCreatedAt(LocalDateTime.now());

        String result = order.toString();
        assertNotNull(result);
        assertEquals("Order{id=1, customerId=101, status=PENDING, totalAmount=250.75, createdAt=" + order.getCreatedAt() + "}", result);
    }
}

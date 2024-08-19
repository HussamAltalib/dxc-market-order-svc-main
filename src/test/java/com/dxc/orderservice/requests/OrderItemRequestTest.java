package com.dxc.orderservice.requests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderItemRequestTest {

    private OrderItemRequest orderItemRequest;

    @BeforeEach
    void setUp() {
        orderItemRequest = new OrderItemRequest();
    }

    @Test
    void testProductId() {
        Integer expectedProductId = 5;
        orderItemRequest.setProductId(expectedProductId);

        assertEquals(expectedProductId, orderItemRequest.getProductId());
    }

    @Test
    void testQuantity() {
        int expectedQuantity = 10;
        orderItemRequest.setQuantity(expectedQuantity);

        assertEquals(expectedQuantity, orderItemRequest.getQuantity());
    }

    @Test
    void testToString() {
        orderItemRequest.setProductId(5);
        orderItemRequest.setQuantity(10);

        String expectedString = "OrderItemRequest{productId=5, quantity=10}";
        assertEquals(expectedString, orderItemRequest.toString());
    }

    @Test
    void testEqualsAndHashCode() {
        OrderItemRequest item1 = new OrderItemRequest();
        item1.setProductId(5);
        item1.setQuantity(10);

        OrderItemRequest item2 = new OrderItemRequest();
        item2.setProductId(5);
        item2.setQuantity(10);

        assertEquals(item1, item2);
        assertEquals(item1.hashCode(), item2.hashCode());

        item2.setQuantity(20);

        assertNotEquals(item1, item2);
        assertNotEquals(item1.hashCode(), item2.hashCode());
    }
}

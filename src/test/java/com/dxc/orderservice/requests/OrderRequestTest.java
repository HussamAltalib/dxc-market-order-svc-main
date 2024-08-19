package com.dxc.orderservice.requests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class OrderRequestTest {

    private OrderRequest orderRequest;

    @BeforeEach
    void setUp() {
        orderRequest = new OrderRequest();
    }

    @Test
    void testCustomerId() {
        int expectedCustomerId = 123;
        orderRequest.setCustomerId(expectedCustomerId);

        assertEquals(expectedCustomerId, orderRequest.getCustomerId());
    }

    @Test
    void testItems() {
        OrderItemRequest item1 = new OrderItemRequest();
        item1.setProductId(5);
        item1.setQuantity(10);

        OrderItemRequest item2 = new OrderItemRequest();
        item2.setProductId(6);
        item2.setQuantity(15);

        orderRequest.setItems(Arrays.asList(item1, item2));

        assertEquals(2, orderRequest.getItems().size());
        assertTrue(orderRequest.getItems().contains(item1));
        assertTrue(orderRequest.getItems().contains(item2));
    }



}

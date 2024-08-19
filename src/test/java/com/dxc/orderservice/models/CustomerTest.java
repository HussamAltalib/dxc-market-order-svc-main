package com.dxc.orderservice.models;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class CustomerTest {

    @Test
    public void testGettersAndSetters() {
        // Create a Customer instance
        Customer customer = new Customer();

        // Set values using setters
        int id = 1;
        String name = "John Doe";
        String phoneNumber = "123456789";
        String email = "johndoe@example.com";

        customer.setCustomerId(id);
        customer.setName(name);
        customer.setPhoneNumber(phoneNumber);
        customer.setEmail(email);

        // Verify values using getters
        Assertions.assertEquals(id, customer.getCustomerId());
        Assertions.assertEquals(name, customer.getName());
        Assertions.assertEquals(phoneNumber, customer.getPhoneNumber());
        Assertions.assertEquals(email, customer.getEmail());
    }
}
package com.dxc.orderservice.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProductTest {

    private Product product;

    @BeforeEach
    public void setUp() {
        product = new Product();
    }

    @Test
    public void testId() {
        int expectedId = 1;
        product.setProductId(expectedId);
        assertEquals(expectedId, product.getProductId());
    }

    @Test
    public void testName() {
        String expectedName = "TestProduct";
        product.setName(expectedName);
        assertEquals(expectedName, product.getName());
    }

    @Test
    public void testPrice() {
        double expectedPrice = 100.50;
        product.setPrice(expectedPrice);
        assertEquals(expectedPrice, product.getPrice());
    }

    @Test
    public void testCategoryName() {
        String expectedCategoryName = "TestCategory";
        product.setCategoryName(expectedCategoryName);
        assertEquals(expectedCategoryName, product.getCategoryName());
    }

    @Test
    public void testFullConstructor() {
        int expectedId = 2;
        String expectedName = "FullProduct";
        double expectedPrice = 200.50;
        String expectedCategoryName = "FullCategory";
        int expectedStock = 100;

        product = new Product(expectedId, expectedName, (float)expectedPrice, expectedCategoryName, expectedStock);
        assertEquals(expectedId, product.getProductId());
        assertEquals(expectedName, product.getName());
        assertEquals(expectedPrice, product.getPrice());
        assertEquals(expectedCategoryName, product.getCategoryName());
    }
}

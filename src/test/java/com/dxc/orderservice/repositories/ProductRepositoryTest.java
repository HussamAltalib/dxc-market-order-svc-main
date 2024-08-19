package com.dxc.orderservice.repositories;

import com.dxc.orderservice.models.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepo;

    @BeforeEach
    void teardown() {
        productRepo.deleteAll();
    }

    @Test
    void shouldSaveAndFetchProduct() {
        // given
        Product product = new Product();
        product.setProductId(1);
        product.setName("SampleProduct");
        product.setPrice(100.00);
        product.setCategoryName("sampleCategory");

        productRepo.save(product);

        // when
        boolean exists = productRepo.existsById(1);

        // then
        assertThat(exists).isTrue();
    }

    @Test
    void shouldReturnFalseForNonExistentProduct() {
        // given
        int productId = 9;

        // when
        boolean exists = productRepo.existsById(productId);

        // then
        assertThat(exists).isFalse();
    }
}

package com.dxc.orderservice.repositories;

import com.dxc.orderservice.models.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepo;

    @BeforeEach
    void teardown() {
        customerRepo.deleteAll();
    }

    @Test
    void shouldSaveAndFetchCustomer() {
        // given
        Customer customer = new Customer();
        customer.setName("John Doe");
        customer.setPhoneNumber("123-456-7890");
        customer.setEmail("johndoe@example.com");
        customerRepo.save(customer);

        // when
        boolean exists = customerRepo.existsById(customer.getCustomerId());

        // then
        assertThat(exists).isTrue();
    }

    @Test
    void shouldReturnFalseForNonExistentCustomer() {
        // given
        int customerId = 999;

        // when
        boolean exists = customerRepo.existsById(customerId);

        // then
        assertThat(exists).isFalse();
    }
}

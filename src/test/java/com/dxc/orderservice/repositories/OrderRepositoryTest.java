package com.dxc.orderservice.repositories;

import com.dxc.orderservice.models.Order;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepo;


    @BeforeEach// Before each test execution, this method will run to ensure a clean test environment.
    void teardown() {
        orderRepo.deleteAll();//Deleting all records in the repository to ensure a clean slate for each test.
    }

    @Test
    void checkValidGetOrderById(){
        //given
        LocalDateTime now = LocalDateTime.now();
        Order order = new Order(1, 1, Order.OrderStatus.PENDING, 758.98, now);
        orderRepo.save(order);
        //when
        boolean exists = orderRepo.existsById(1);
        //then
        assertThat(exists).isTrue();
    }

    @Test
    void checkInvalidGetOrderById(){
        //given
        int orderId = 9;
        //when
        boolean exists = orderRepo.existsById(9);
        //then
        assertThat(exists).isFalse();
    }


}
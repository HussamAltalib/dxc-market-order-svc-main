package com.dxc.orderservice.repositories;

import com.dxc.orderservice.models.OrderItem;
import com.dxc.orderservice.models.OrderItemCompositeKey;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class OrderItemRepositoryTest {

    @Autowired
    private OrderItemRepository orderItemRepo;

    @BeforeEach// Before each test execution, this method will run to ensure a clean test environment.
    void teardown() {
        orderItemRepo.deleteAll();//Deleting all records in the repository to ensure a clean slate for each test.
    }

    @Test
    void checkValidOrderItem() {
        //given
        OrderItem orderItem = new OrderItem();
        OrderItemCompositeKey compositeKey = new OrderItemCompositeKey();

        compositeKey.setOrderId(1);
        compositeKey.setProductId(1);

        orderItem.setId(compositeKey);
        orderItem.setQuantity(5);

        orderItemRepo.save(orderItem);
        //when
        boolean exists = orderItemRepo.existsById(compositeKey);
        //then
        assertThat(exists).isTrue();
    }

    @Test
    void checkInvalidOrderItem() {
        //given
        OrderItem orderItem = new OrderItem();
        OrderItemCompositeKey compositeKey = new OrderItemCompositeKey();

        compositeKey.setOrderId(5);
        compositeKey.setProductId(5);

        orderItem.setId(compositeKey);
        orderItem.setQuantity(5);

        //orderItemRepo.save(orderItem);
        //when
        boolean exists = orderItemRepo.existsById(compositeKey);
        //then
        assertThat(exists).isFalse();
    }

    @Test
    void checkListOrderItemGivenOrderId() {
        // given
        int OrderId = 1;

        for (int i = 0; i < 10; i++) {
            OrderItem orderItem = new OrderItem();
            OrderItemCompositeKey compositeKey = new OrderItemCompositeKey();

            compositeKey.setOrderId(OrderId);
            compositeKey.setProductId(i);

            orderItem.setId(compositeKey);
            orderItem.setQuantity(3);

            orderItemRepo.save(orderItem);
        }
        //when
        List<OrderItem> orderItems = orderItemRepo.findByIdOrderId(OrderId);
        // then
        Assertions.assertThat(orderItems).hasSize(10);

        for (OrderItem item : orderItems) {
            Assertions.assertThat(item.getQuantity()).isEqualTo(3);
        }
    }
}
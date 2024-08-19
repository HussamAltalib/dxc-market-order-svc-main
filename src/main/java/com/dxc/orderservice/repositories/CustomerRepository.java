package com.dxc.orderservice.repositories;


import com.dxc.orderservice.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Customer getCustomerByCustomerId(int customerId);
}

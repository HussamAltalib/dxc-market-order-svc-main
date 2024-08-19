package com.dxc.orderservice.repositories;

import com.dxc.orderservice.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;


public interface  ProductRepository extends JpaRepository<Product, Integer> {

    Product getProductByProductId(int id);

    @Query("SELECT p FROM Product p")
    List<Product> findAll();

    @Query("SELECT p.id FROM Product p")
    List<Integer> getAllProductsIds();



}

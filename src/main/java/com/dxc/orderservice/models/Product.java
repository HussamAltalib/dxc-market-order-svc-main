package com.dxc.orderservice.models;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "products")
public final class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int productId;
    @Column(name = "name")
    private String name;
    @Column(name = "price")
    private double price;
    @Column(name = "category_name")
    private String categoryName;





    public Product() {

    }
    public Product(final int productId, final String name, final double price,
                   final String categoryName, final int stock) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.categoryName = categoryName;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(final int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }
    public void setName(final String name) {
        this.name = name;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(final double price) {
        this.price = price;
    }
    public String getCategoryName() {
        return categoryName;
    }
    public void setCategoryName(final String categoryName) {
        this.categoryName = categoryName;
    }

}

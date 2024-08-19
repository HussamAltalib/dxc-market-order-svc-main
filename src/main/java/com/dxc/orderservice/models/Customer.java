package com.dxc.orderservice.models;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;


@Entity
@Table(name = "customers")
public final class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int customerId;

    @Column(name = "name")
    private String name;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email")
    private String email;


    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(final int customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }


    public void setName(final String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(final String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(final String email) {
        this.email = email;
    }


}


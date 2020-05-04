package com.yuyi.tea.bean;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(value = { "handler" })
public class Address implements Serializable {

    private int uid;
    private String address;
    private Customer customer;
    private boolean isDefault;

    public Address() {
    }

    public Address(int uid) {
        this.uid = uid;
    }

    public Address(int uid, String address, Customer customer, boolean isDefault) {
        this.uid = uid;
        this.address = address;
        this.customer = customer;
        this.isDefault = isDefault;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "Address{" +
                "uid=" + uid +
                ", address='" + address + '\'' +
                ", customer=" + customer +
                ", isDefault=" + isDefault +
                '}';
    }
}

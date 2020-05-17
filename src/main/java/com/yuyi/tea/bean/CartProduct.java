package com.yuyi.tea.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(value = { "handler" })
public class CartProduct implements Serializable {

    private int uid;
    private Customer customer;
    private Product product;
    private int number;

    public CartProduct() {
    }

    public CartProduct(int uid) {
        this.uid = uid;
    }

    public CartProduct(int uid, Customer customer, Product product, int number) {
        this.uid = uid;
        this.customer = customer;
        this.product = product;
        this.number = number;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "CartProduct{" +
                "uid=" + uid +
                ", customer=" + customer +
                ", product=" + product +
                ", number=" + number +
                '}';
    }
}

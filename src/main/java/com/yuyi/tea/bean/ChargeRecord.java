package com.yuyi.tea.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(value = { "handler" })
public class ChargeRecord implements Serializable {
    private int uid;
    private float amount;
    private long time;
    private Customer customer;

    public ChargeRecord() {
    }

    public ChargeRecord(int uid) {
        this.uid = uid;
    }

    public ChargeRecord(int uid, float amount, long time, Customer customer) {
        this.uid = uid;
        this.amount = amount;
        this.time = time;
        this.customer = customer;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}

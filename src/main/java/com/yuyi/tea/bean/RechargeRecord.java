package com.yuyi.tea.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;


@JsonIgnoreProperties(value = { "handler" })
public class RechargeRecord implements Serializable {

    private int uid;
    private float amount;
    private Customer customer;
    private long time;

    public RechargeRecord() {
    }

    public RechargeRecord(int uid) {
        this.uid = uid;
    }

    public RechargeRecord(int uid, float amount, Customer customer, long time) {
        this.uid = uid;
        this.amount = amount;
        this.customer = customer;
        this.time = time;
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "RechargeRecord{" +
                "uid=" + uid +
                ", amount=" + amount +
                ", customer=" + customer +
                ", time=" + time +
                '}';
    }
}

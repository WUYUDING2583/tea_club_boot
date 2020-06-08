package com.yuyi.tea.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(value = { "handler" })
public class BillDetail implements Serializable {

    private int uid;
    private long time;
    private float ingot;
    private float credit;
    private BillDescription description;
    private User customer;

    public BillDetail() {
    }

    public BillDetail(int uid) {
        this.uid = uid;
    }

    public BillDetail(int uid, long time, float ingot, float credit, BillDescription description, User customer) {
        this.uid = uid;
        this.time = time;
        this.ingot = ingot;
        this.credit = credit;
        this.description = description;
        this.customer = customer;
    }

    public BillDetail(long time, float ingot, float credit, BillDescription description, User customer) {
        this.time = time;
        this.ingot = ingot;
        this.credit = credit;
        this.description = description;
        this.customer = customer;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public float getIngot() {
        return ingot;
    }

    public void setIngot(float ingot) {
        this.ingot = ingot;
    }

    public float getCredit() {
        return credit;
    }

    public void setCredit(float credit) {
        this.credit = credit;
    }

    public BillDescription getDescription() {
        return description;
    }

    public void setDescription(BillDescription description) {
        this.description = description;
    }
}

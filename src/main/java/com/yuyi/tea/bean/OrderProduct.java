package com.yuyi.tea.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(value = { "handler" })
public class OrderProduct implements Serializable {

    private int uid;
    private Product product;
    private int number;
    private ActivityRule activityRule;//订单中此产品实际参与的活动

    public OrderProduct() {
    }

    public OrderProduct(int uid) {
        this.uid = uid;
    }

    public OrderProduct(int uid, Product product, int number, ActivityRule activityRule) {
        this.uid = uid;
        this.product = product;
        this.number = number;
        this.activityRule = activityRule;
    }

    public ActivityRule getActivityRule() {
        return activityRule;
    }

    public void setActivityRule(ActivityRule activityRule) {
        this.activityRule = activityRule;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
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
        return "OrderProduct{" +
                "uid=" + uid +
                ", product=" + product +
                ", number=" + number +
                ", activityRule=" + activityRule +
                '}';
    }
}

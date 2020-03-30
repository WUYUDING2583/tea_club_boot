package com.yuyi.tea.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yuyi.tea.component.Amount;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(value = { "handler" })
public class Order implements Serializable {

    private int uid;
    private long orderTime;
    private Customer customer;
    private Clerk clerk;
    private String status;
    private List<OrderProduct> products;
    private ActivityRule activityRule;//全局购物满减活动规则
    private List<Activity> activities;
    private Amount amount;

    public Order() {
    }

    public Order(int uid) {
        this.uid = uid;
    }

    public Order(int uid, long orderTime, Customer customer, Clerk clerk, String status, List<OrderProduct> products, ActivityRule activityRule, List<Activity> activities, Amount amount) {
        this.uid = uid;
        this.orderTime = orderTime;
        this.customer = customer;
        this.clerk = clerk;
        this.status = status;
        this.products = products;
        this.activityRule = activityRule;
        this.activities = activities;
        this.amount = amount;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public ActivityRule getActivityRule() {
        return activityRule;
    }

    public void setActivityRule(ActivityRule activityRule) {
        this.activityRule = activityRule;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public long getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(long orderTime) {
        this.orderTime = orderTime;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Clerk getClerk() {
        return clerk;
    }

    public void setClerk(Clerk clerk) {
        this.clerk = clerk;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<OrderProduct> getProducts() {
        return products;
    }

    public void setProducts(List<OrderProduct> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "Order{" +
                "uid=" + uid +
                ", orderTime=" + orderTime +
                ", customer=" + customer +
                ", clerk=" + clerk +
                ", status='" + status + '\'' +
                ", products=" + products +
                ", activityRule=" + activityRule +
                ", activities=" + activities +
                ", amount=" + amount +
                '}';
    }
}

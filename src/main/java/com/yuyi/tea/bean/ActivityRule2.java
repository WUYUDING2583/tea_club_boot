package com.yuyi.tea.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(value = { "handler" })
public class ActivityRule2 implements Serializable {

    private int uid;
    private float number;
    private String currency;
    private String operation;

    public ActivityRule2() {
    }

    public ActivityRule2(int uid) {
        this.uid = uid;
    }

    public ActivityRule2(int uid, float number, String currency, String operation) {
        this.uid = uid;
        this.number = number;
        this.currency = currency;
        this.operation = operation;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public float getNumber() {
        return number;
    }

    public void setNumber(float number) {
        this.number = number;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    @Override
    public String toString() {
        return "ActivityRule2{" +
                "uid=" + uid +
                ", number=" + number +
                ", currency='" + currency + '\'' +
                ", operation='" + operation + '\'' +
                '}';
    }
}

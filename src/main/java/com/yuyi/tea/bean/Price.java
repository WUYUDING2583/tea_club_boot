package com.yuyi.tea.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(value = { "handler" })
public class Price implements Serializable {

    private int uid;
    private float ingot;//元宝
    private float credit;//积分

    public Price() {
    }

    public Price(int uid, float ingot, float credit) {
        this.uid = uid;
        this.ingot = ingot;
        this.credit = credit;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
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

    @Override
    public String toString() {
        return "Price{" +
                "ingot=" + ingot +
                ", credit=" + credit +
                '}';
    }
}

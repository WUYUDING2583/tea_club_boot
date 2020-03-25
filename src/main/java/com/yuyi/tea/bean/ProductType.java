package com.yuyi.tea.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(value = { "handler" })
public class ProductType implements Serializable {

    private int uid;
    private String name;

    public ProductType() {
    }

    public ProductType(int uid) {
        this.uid = uid;
    }

    public ProductType(int uid, String name) {
        this.uid = uid;
        this.name = name;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ProductType{" +
                "uid=" + uid +
                ", name='" + name + '\'' +
                '}';
    }
}

package com.yuyi.tea.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(value = { "handler" })
public class BillDescription implements Serializable {

    private int uid;
    private String description;

    public BillDescription() {
    }

    public BillDescription(int uid) {
        this.uid = uid;
    }

    public BillDescription(int uid, String description) {
        this.uid = uid;
        this.description = description;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

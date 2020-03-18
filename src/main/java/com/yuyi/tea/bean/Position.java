package com.yuyi.tea.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(value = { "handler" })
public class Position implements Serializable {

    private int uid;
    private String name;

    public Position(int uid, String name) {
        this.uid = uid;
        this.name = name;
    }

    public Position() {
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
        return "Position{" +
                "uid=" + uid +
                ", name='" + name + '\'' +
                '}';
    }
}

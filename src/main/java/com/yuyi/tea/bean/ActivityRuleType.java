package com.yuyi.tea.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(value = { "handler" })
public class ActivityRuleType implements Serializable {

    private int uid;
    private String name;

    public ActivityRuleType() {
    }

    public ActivityRuleType(int uid) {
        this.uid = uid;
    }

    public ActivityRuleType(int uid, String name) {
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
        return "ActivityRuleType{" +
                "uid=" + uid +
                ", name='" + name + '\'' +
                '}';
    }
}

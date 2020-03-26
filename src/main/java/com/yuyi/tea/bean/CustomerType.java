package com.yuyi.tea.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(value = { "handler" })
public class CustomerType implements Serializable {

    private int uid;
    private String name;
    private List<ActivityRule> activityRules=new ArrayList<>();

    public CustomerType() {
    }

    public CustomerType(int uid) {
        this.uid = uid;
    }

    public CustomerType(int uid, String name) {
        this.uid = uid;
        this.name = name;
    }

    public CustomerType(int uid, String name, List<ActivityRule> activityRules) {
        this.uid = uid;
        this.name = name;
        this.activityRules = activityRules;
    }

    public List<ActivityRule> getActivityRules() {
        return activityRules;
    }

    public void setActivityRules(List<ActivityRule> activityRules) {
        this.activityRules = activityRules;
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
        return "CustomerType{" +
                "uid=" + uid +
                ", name='" + name + '\'' +
                ", activityRules=" + activityRules +
                '}';
    }
}

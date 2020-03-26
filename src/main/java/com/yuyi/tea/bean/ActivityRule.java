package com.yuyi.tea.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(value = { "handler" })
public class ActivityRule implements Serializable {

    private int uid;
    private ActivityRuleType activityRuleType;
    private List<Product> activityApplyForProduct;
    private List<CustomerType> activityApplyForCustomerTypes;
    private float activityRule1;
    private ActivityRule2 activityRule2;//活动规则的详细增减信息

    public ActivityRule() {
    }
    public ActivityRule(int uid) {
        this.uid = uid;
    }

    public ActivityRule(int uid, ActivityRuleType activityRuleType, List<Product> activityApplyForProduct, List<CustomerType> activityApplyForCustomerTypes, float activityRule1, ActivityRule2 activityRule2) {
        this.uid = uid;
        this.activityRuleType = activityRuleType;
        this.activityApplyForProduct = activityApplyForProduct;
        this.activityApplyForCustomerTypes = activityApplyForCustomerTypes;
        this.activityRule1 = activityRule1;
        this.activityRule2 = activityRule2;
    }

    public ActivityRuleType getActivityRuleType() {
        return activityRuleType;
    }

    public void setActivityRuleType(ActivityRuleType activityRuleType) {
        this.activityRuleType = activityRuleType;
    }

    public List<Product> getActivityApplyForProduct() {
        return activityApplyForProduct;
    }

    public void setActivityApplyForProduct(List<Product> activityApplyForProduct) {
        this.activityApplyForProduct = activityApplyForProduct;
    }

    public List<CustomerType> getActivityApplyForCustomerTypes() {
        return activityApplyForCustomerTypes;
    }

    public void setActivityApplyForCustomerTypes(List<CustomerType> activityApplyForCustomerTypes) {
        this.activityApplyForCustomerTypes = activityApplyForCustomerTypes;
    }

    public float getActivityRule1() {
        return activityRule1;
    }

    public void setActivityRule1(float activityRule1) {
        this.activityRule1 = activityRule1;
    }

    public ActivityRule2 getActivityRule2() {
        return activityRule2;
    }

    public void setActivityRule2(ActivityRule2 activityRule2) {
        this.activityRule2 = activityRule2;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "ActivityRule{" +
                "uid=" + uid +
                ", activityRuleType=" + activityRuleType +
                ", activityApplyForProduct=" + activityApplyForProduct +
                ", activityApplyForCustomerTypes=" + activityApplyForCustomerTypes +
                ", activityRule1=" + activityRule1 +
                ", activityRule2=" + activityRule2 +
                '}';
    }
}

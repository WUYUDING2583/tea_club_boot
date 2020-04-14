package com.yuyi.tea.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(value = { "handler" })
public class Customer extends User {

    private String email;
    private CustomerType customerType;
    private List<EnterpriseCustomerApplication> enterpriseCustomerApplications;
    private String weChatId;

    public Customer() {
    }

    public Customer(int uid) {
        super(uid);
    }

    public Customer(int uid, String name, String contact, String identityId, int gender, String address, Photo avatar, String password, boolean enforceTerminal, String email, CustomerType customerType, List<EnterpriseCustomerApplication> enterpriseCustomerApplications, String weChatId) {
        super(uid, name, contact, identityId, gender, address, avatar, password, enforceTerminal);
        this.email = email;
        this.customerType = customerType;
        this.enterpriseCustomerApplications = enterpriseCustomerApplications;
        this.weChatId = weChatId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public CustomerType getCustomerType() {
        return customerType;
    }

    public void setCustomerType(CustomerType customerType) {
        this.customerType = customerType;
    }

    public List<EnterpriseCustomerApplication> getEnterpriseCustomerApplications() {
        return enterpriseCustomerApplications;
    }

    public void setEnterpriseCustomerApplications(List<EnterpriseCustomerApplication> enterpriseCustomerApplications) {
        this.enterpriseCustomerApplications = enterpriseCustomerApplications;
    }

    public String getWeChatId() {
        return weChatId;
    }

    public void setWeChatId(String weChatId) {
        this.weChatId = weChatId;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "email='" + email + '\'' +
                ", customerType=" + customerType +
                ", enterpriseCustomerApplications=" + enterpriseCustomerApplications +
                ", weChatId='" + weChatId + '\'' +
                '}';
    }
}

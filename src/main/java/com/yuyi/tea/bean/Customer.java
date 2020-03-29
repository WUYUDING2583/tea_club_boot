package com.yuyi.tea.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(value = { "handler" })
public class Customer implements Serializable {

    private int uid;
    private String name;
    private String contact;
    private String identityId;
    private String email;
    private CustomerType customerType;
    private int gender;
    private Photo avatar;
    private String address;
    private List<EnterpriseCustomerApplication> enterpriseCustomerApplications;
    private String password;
    private String weChatId;

    public Customer() {
    }

    public Customer(int uid) {
        this.uid = uid;
    }

    public Customer(int uid, String name, String contact, String identityId, String email, CustomerType customerType, int gender, Photo avatar, String address, List<EnterpriseCustomerApplication> enterpriseCustomerApplications, String password, String weChatId) {
        this.uid = uid;
        this.name = name;
        this.contact = contact;
        this.identityId = identityId;
        this.email = email;
        this.customerType = customerType;
        this.gender = gender;
        this.avatar = avatar;
        this.address = address;
        this.enterpriseCustomerApplications = enterpriseCustomerApplications;
        this.password = password;
        this.weChatId = weChatId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getWeChatId() {
        return weChatId;
    }

    public void setWeChatId(String weChatId) {
        this.weChatId = weChatId;
    }

    public List<EnterpriseCustomerApplication> getEnterpriseCustomerApplications() {
        return enterpriseCustomerApplications;
    }

    public void setEnterpriseCustomerApplications(List<EnterpriseCustomerApplication> enterpriseCustomerApplications) {
        this.enterpriseCustomerApplications = enterpriseCustomerApplications;
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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getIdentityId() {
        return identityId;
    }

    public void setIdentityId(String identityId) {
        this.identityId = identityId;
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

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public Photo getAvatar() {
        return avatar;
    }

    public void setAvatar(Photo avatar) {
        this.avatar = avatar;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "uid=" + uid +
                ", name='" + name + '\'' +
                ", contact='" + contact + '\'' +
                ", identityId='" + identityId + '\'' +
                ", email='" + email + '\'' +
                ", customerType=" + customerType +
                ", gender=" + gender +
                ", avatar=" + avatar +
                ", address='" + address + '\'' +
                ", enterpriseCustomerApplications=" + enterpriseCustomerApplications +
                ", weChatId='" + weChatId + '\'' +
                '}';
    }
}

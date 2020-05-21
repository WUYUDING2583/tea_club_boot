package com.yuyi.tea.bean;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(value = { "handler" })
public class Address implements Serializable {

    private int uid;
    private String province;
    private String city;
    private String district;
    private String detail;
    private String phone;
    private Customer customer;
    private boolean isDefaultAddress;
    private String name;

    public Address() {
    }

    public Address(int uid) {
        this.uid = uid;
    }

    public Address(int uid, String province, String city, String district, String detail, String phone, Customer customer, boolean isDefaultAddress, String name) {
        this.uid = uid;
        this.province = province;
        this.city = city;
        this.district = district;
        this.detail = detail;
        this.phone = phone;
        this.customer = customer;
        this.isDefaultAddress = isDefaultAddress;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean getIsDefaultAddress() {
        return isDefaultAddress;
    }

    public void setIsDefaultAddress(boolean defaultAddress) {
        isDefaultAddress = defaultAddress;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

}

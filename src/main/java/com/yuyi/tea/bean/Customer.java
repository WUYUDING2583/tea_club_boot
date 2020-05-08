package com.yuyi.tea.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yuyi.tea.common.Amount;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(value = { "handler" })
public class Customer extends User {

    private String email;
    private CustomerType customerType;
    private List<EnterpriseCustomerApplication> enterpriseCustomerApplications;
    private String weChatId;
    private List<Order> orders;
    private List<Address> addresses;
    private Amount balance=new Amount();
    private float ingot=0;
    private float credit=0;

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public Customer() {
    }

    public Customer(int uid) {
        super(uid);
    }

    public Customer(int uid, String name, String contact, String identityId, int gender, String address, Photo avatar, String password, boolean enforceTerminal, String email, CustomerType customerType, List<EnterpriseCustomerApplication> enterpriseCustomerApplications, String weChatId, List<Order> orders, List<Address> addresses, Amount balance, float ingot, float credit) {
        super(uid, name, contact, identityId, gender, address, avatar, password, enforceTerminal);
        this.email = email;
        this.customerType = customerType;
        this.enterpriseCustomerApplications = enterpriseCustomerApplications;
        this.weChatId = weChatId;
        this.orders = orders;
        this.addresses = addresses;
        this.balance = balance;
        this.ingot = ingot;
        this.credit = credit;
    }

    public float getIngot() {
        return balance.getIngot();
    }

    public void setIngot(float ingot) {
        this.balance.setIngot(ingot);
    }

    public float getCredit() {
        return balance.getCredit();
    }

    public void setCredit(float credit) {
        this.balance.setCredit(credit);
    }

    public Amount getBalance() {
        return balance;
    }

    public void setBalance(Amount balance) {
        this.balance = balance;
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

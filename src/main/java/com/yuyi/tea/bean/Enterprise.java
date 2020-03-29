package com.yuyi.tea.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(value = { "handler" })
public class Enterprise implements Serializable {

    private int uid;
    private String name;
    private String contact;
    private String email;
    private String address;
    private Photo  businessLicense;

    public Enterprise() {
    }

    public Enterprise(int uid) {
        this.uid = uid;
    }

    public Enterprise(int uid, String name, String contact, String email, String address, Photo businessLicense) {
        this.uid = uid;
        this.name = name;
        this.contact = contact;
        this.email = email;
        this.address = address;
        this.businessLicense = businessLicense;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Photo getBusinessLicense() {
        return businessLicense;
    }

    public void setBusinessLicense(Photo businessLicense) {
        this.businessLicense = businessLicense;
    }

    @Override
    public String toString() {
        return "Enterprise{" +
                "uid=" + uid +
                ", name='" + name + '\'' +
                ", contact='" + contact + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", businessLicense=" + businessLicense +
                '}';
    }
}

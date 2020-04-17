package com.yuyi.tea.bean;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {

    private int uid;
    private String name;
    private String contact;
    private String identityId;
    private int gender;
    private String address;
    private Photo avatar;
    private String password;
    private boolean enforceTerminal=false;
    private String token;

    public User() {
    }

    public User(int uid) {
        this.uid = uid;
    }

    public User(int uid, String name, String contact, String identityId, int gender, String address, Photo avatar, String password, boolean enforceTerminal) {
        this.uid = uid;
        this.name = name;
        this.contact = contact;
        this.identityId = identityId;
        this.gender = gender;
        this.address = address;
        this.avatar = avatar;
        this.password = password;
        this.enforceTerminal = enforceTerminal;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isEnforceTerminal() {
        return enforceTerminal;
    }

    public void setEnforceTerminal(boolean enforceTerminal) {
        this.enforceTerminal = enforceTerminal;
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

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Photo getAvatar() {
        return avatar;
    }

    public void setAvatar(Photo avatar) {
        this.avatar = avatar;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", name='" + name + '\'' +
                ", contact='" + contact + '\'' +
                ", identityId='" + identityId + '\'' +
                ", gender=" + gender +
                ", address='" + address + '\'' +
                ", avatar=" + avatar +
                ", password='" + password + '\'' +
                ", enforceTerminal=" + enforceTerminal +
                '}';
    }
}

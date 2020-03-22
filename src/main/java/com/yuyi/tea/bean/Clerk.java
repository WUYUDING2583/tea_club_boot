package com.yuyi.tea.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(value = { "handler" })
public class Clerk implements Serializable {

    private int uid;
    private String name;
    private Position position;
    private String contact;
    private String identityId;
    private int sex;
    private Photo avatar;
    private String address;
    private Shop shop;

    public Clerk() {
    }

    public Clerk(int uid) {
        this.uid = uid;
    }

    public Clerk(int uid, String name, Position position) {
        this.uid = uid;
        this.name = name;
        this.position = position;
    }

    public Clerk(int uid, String name, Position position, String contact, String identityId, int sex, Photo avatar, String address, Shop shop) {
        this.uid = uid;
        this.name = name;
        this.position = position;
        this.contact = contact;
        this.identityId = identityId;
        this.sex = sex;
        this.avatar = avatar;
        this.address = address;
        this.shop = shop;
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

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
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

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
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

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    @Override
    public String toString() {
        return "Clerk{" +
                "uid=" + uid +
                ", name='" + name + '\'' +
                ", position=" + position +
                ", contact='" + contact + '\'' +
                ", identityId='" + identityId + '\'' +
                ", sex=" + sex +
                ", avatar=" + avatar +
                ", address='" + address + '\'' +
                ", shop=" + shop +
                '}';
    }
}

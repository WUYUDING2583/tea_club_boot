package com.yuyi.tea.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(value = { "handler" })
public class Photo implements Serializable {


    private int uid;
    private byte[] photo;
    private int shopId;

    public Photo(int uid, byte[] photo, int shopId) {
        this.uid = uid;
        this.photo = photo;
        this.shopId = shopId;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public int getUid() {
        return uid;
    }

    public Photo() {
    }

    public Photo(int uid, byte[] photo) {
        this.uid = uid;
        this.photo = photo;
    }

    public Photo(int uid) {
        this.uid = uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }
}

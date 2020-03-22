package com.yuyi.tea.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Arrays;

@JsonIgnoreProperties(value = { "handler" })
public class Photo implements Serializable {


    private int uid;
    private byte[] photo;
    private int shopId;
    private int shopBoxId;
    private int clerkId;

    public Photo() {
    }

    public Photo(int uid) {
        this.uid = uid;
    }

    public Photo(int uid, byte[] photo) {
        this.uid = uid;
        this.photo = photo;
    }

    public int getShopBoxId() {
        return shopBoxId;
    }

    public void setShopBoxId(int shopBoxId) {
        this.shopBoxId = shopBoxId;
    }

    public int getClerkId() {
        return clerkId;
    }

    public void setClerkId(int clerkId) {
        this.clerkId = clerkId;
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

    public void setUid(int uid) {
        this.uid = uid;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "uid=" + uid +
                ", photo=" + Arrays.toString(photo) +
                ", shopId=" + shopId +
                ", shopBoxId=" + shopBoxId +
                ", clerkId=" + clerkId +
                '}';
    }
}

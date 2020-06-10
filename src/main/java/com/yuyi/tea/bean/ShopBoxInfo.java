package com.yuyi.tea.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(value = { "handler" })
public class ShopBoxInfo implements Serializable {

    private int uid;
    private String title;
    private String info;
    private int boxId;

    public ShopBoxInfo() {
    }

    public ShopBoxInfo(int uid) {
        this.uid = uid;
    }

    public ShopBoxInfo(int uid, String title, String info, int boxId) {
        this.uid = uid;
        this.title = title;
        this.info = info;
        this.boxId = boxId;
    }

    public int getBoxId() {
        return boxId;
    }

    public void setBoxId(int boxId) {
        this.boxId = boxId;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}

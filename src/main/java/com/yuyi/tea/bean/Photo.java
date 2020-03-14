package com.yuyi.tea.bean;

import java.io.Serializable;

public class Photo implements Serializable {

    public int getUid() {
        return uid;
    }

    public Photo() {
    }

    public Photo(int uid, byte[] photo) {
        this.uid = uid;
        this.photo = photo;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    private int uid;


    private byte[] photo;



    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }
}

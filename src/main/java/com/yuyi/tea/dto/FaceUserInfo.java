package com.yuyi.tea.dto;


import com.yuyi.tea.bean.Clerk;
import com.yuyi.tea.bean.Customer;

public class FaceUserInfo {

    private int uid;
    private String faceId;
    private Integer similarValue;
    private byte[] faceFeature;
    private Customer customer=null;
    private Clerk clerk=null;
    private byte[] face;

    public byte[] getFace() {
        return face;
    }

    public void setFace(byte[] face) {
        this.face = face;
    }

    public String getFaceId() {
        return faceId;
    }

    public void setFaceId(String faceId) {
        this.faceId = faceId;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Clerk getClerk() {
        return clerk;
    }

    public void setClerk(Clerk clerk) {
        this.clerk = clerk;
    }

    public Integer getSimilarValue() {
        return similarValue;
    }

    public void setSimilarValue(Integer similarValue) {
        this.similarValue = similarValue;
    }

    public byte[] getFaceFeature() {
        return faceFeature;
    }

    public void setFaceFeature(byte[] faceFeature) {
        this.faceFeature = faceFeature;
    }

}

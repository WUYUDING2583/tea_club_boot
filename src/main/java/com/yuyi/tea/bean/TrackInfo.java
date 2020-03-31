package com.yuyi.tea.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(value = { "handler" })
public class TrackInfo implements Serializable {

    private int uid;
    private String companyName;
    private String trackingId;
    private String description;
    private String phone;

    public TrackInfo() {
    }

    public TrackInfo(int uid) {
        this.uid = uid;
    }

    public TrackInfo(int uid, String companyName, String trackingId, String description, String phone) {
        this.uid = uid;
        this.companyName = companyName;
        this.trackingId = trackingId;
        this.description = description;
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    @Override
    public String toString() {
        return "TrackInfo{" +
                "uid=" + uid +
                ", companyName='" + companyName + '\'' +
                ", trackingId='" + trackingId + '\'' +
                ", description='" + description + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}

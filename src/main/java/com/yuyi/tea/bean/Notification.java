package com.yuyi.tea.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(value = { "handler" })
public class Notification implements Serializable {

    private int uid;
    private boolean isRead;
    private int type;//0 预约时间临近通知，1 退款成功通知，2 订单发货通知，3 门店自提订单准备完毕通知， 4 充值成功通知
    private String title;
    private String detail;
    private long time;
    private Customer customer;

    public Notification() {
    }

    public Notification(int uid) {
        this.uid = uid;
    }

    public Notification(int uid, boolean isRead, int type, String title, String detail, long time, Customer customer) {
        this.uid = uid;
        this.isRead = isRead;
        this.type = type;
        this.title = title;
        this.detail = detail;
        this.time = time;
        this.customer = customer;
    }

    public Notification(boolean isRead, int type, String title, String detail, long time, Customer customer) {
        this.isRead = isRead;
        this.type = type;
        this.title = title;
        this.detail = detail;
        this.time = time;
        this.customer = customer;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "uid=" + uid +
                ", isRead=" + isRead +
                ", title='" + title + '\'' +
                ", detail='" + detail + '\'' +
                ", time=" + time +
                ", customer=" + customer +
                '}';
    }
}

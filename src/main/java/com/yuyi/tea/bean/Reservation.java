package com.yuyi.tea.bean;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(value = { "handler" })
public class Reservation implements Serializable {

    private int uid;
    private long reservationTime;
    private int boxId;
    private int orderId;

    public Reservation() {
    }

    public Reservation(int uid) {
        this.uid = uid;
    }

    public Reservation(int uid, long reservationTime, int boxId, int orderId) {
        this.uid = uid;
        this.reservationTime = reservationTime;
        this.boxId = boxId;
        this.orderId = orderId;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public long getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(long reservationTime) {
        this.reservationTime = reservationTime;
    }

    public int getBoxId() {
        return boxId;
    }

    public void setBoxId(int boxId) {
        this.boxId = boxId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "uid=" + uid +
                ", reservationTime=" + reservationTime +
                ", boxId=" + boxId +
                ", orderId=" + orderId +
                '}';
    }
}

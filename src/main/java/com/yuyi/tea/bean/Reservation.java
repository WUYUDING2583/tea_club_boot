package com.yuyi.tea.bean;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(value = { "handler" })
public class Reservation implements Serializable {

    private long reservationTime;
    private int boxId;
    private int orderId;

    public Reservation() {
    }

    public Reservation(long reservationTime) {
        this.reservationTime = reservationTime;
    }

    public Reservation(long reservationTime, int boxId, int orderId) {
        this.reservationTime = reservationTime;
        this.boxId = boxId;
        this.orderId = orderId;
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
                "reservationTime=" + reservationTime +
                ", boxId=" + boxId +
                ", orderId=" + orderId +
                '}';
    }
}

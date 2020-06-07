package com.yuyi.tea.bean;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(value = { "handler" })
public class Reservation implements Serializable {

    private long reservationTime;
    private ShopBox box;
    private int orderId;

    public Reservation() {
    }

    public Reservation(long reservationTime) {
        this.reservationTime = reservationTime;
    }

    public Reservation(long reservationTime, ShopBox box, int orderId) {
        this.reservationTime = reservationTime;
        this.box = box;
        this.orderId = orderId;
    }

    public long getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(long reservationTime) {
        this.reservationTime = reservationTime;
    }

    public ShopBox getBox() {
        return box;
    }

    public void setBox(ShopBox box) {
        this.box = box;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }


}

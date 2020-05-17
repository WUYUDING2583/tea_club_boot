package com.yuyi.tea.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.math.BigDecimal;

@JsonIgnoreProperties(value = { "handler" })
public class BoxReservation implements Serializable {

    private int boxId;
    private ShopBox box;
    private BigDecimal number=BigDecimal.valueOf(0);

    public BoxReservation(int boxId) {
        this.boxId = boxId;
    }

    public int getBoxId() {
        return boxId;
    }

    public void setBoxId(int boxId) {
        this.boxId = boxId;
    }

    public ShopBox getBox() {
        return box;
    }

    public void setBox(ShopBox box) {
        this.box = box;
    }

    public BigDecimal getNumber() {
        return number;
    }

    public void setNumber(BigDecimal number) {
        this.number = number;
    }
}

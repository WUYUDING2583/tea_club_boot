package com.yuyi.tea.common;

import java.io.Serializable;

public class Amount implements Serializable {
    private float ingot;
    private float credit;

    public Amount() {
    }

    public Amount(float ingot, float credit) {
        this.ingot = ingot;
        this.credit = credit;
    }

    public float getIngot() {
        return ingot;
    }

    public void setIngot(float ingot) {
        this.ingot = ingot;
    }

    public float getCredit() {
        return credit;
    }

    public void setCredit(float credit) {
        this.credit = credit;
    }

    @Override
    public String toString() {
        return "Amount{" +
                "ingot=" + ingot +
                ", credit=" + credit +
                '}';
    }
}

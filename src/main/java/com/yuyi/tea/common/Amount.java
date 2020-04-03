package com.yuyi.tea.common;

import java.io.Serializable;

public class Amount implements Serializable {
    private float ingot;
    private int credit;

    public Amount() {
    }

    public Amount(float ingot, int credit) {
        this.ingot = ingot;
        this.credit = credit;
    }

    public float getIngot() {
        return ingot;
    }

    public void setIngot(float ingot) {
        this.ingot = ingot;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
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

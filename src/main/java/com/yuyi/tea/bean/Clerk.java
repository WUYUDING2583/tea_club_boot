package com.yuyi.tea.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(value = { "handler" })
public class Clerk  extends User{

    private Position position;
    private Shop shop;
    private List<PositionAutorityFrontDetail> positionAutorityFrontDetails;
    private int leastDiscount;

    public Clerk() {
    }

    public Clerk(int uid) {
        super(uid);
    }

    public Clerk(int uid,String name){
        super(uid,name);
    }

    public Clerk(int uid, String name, String contact, String identityId, int gender, String address, Photo avatar, String password, boolean enforceTerminal, Position position, Shop shop, List<PositionAutorityFrontDetail> positionAutorityFrontDetails, int leastDiscount) {
        super(uid, name, contact, identityId, gender, address, avatar, password, enforceTerminal);
        this.position = position;
        this.shop = shop;
        this.positionAutorityFrontDetails = positionAutorityFrontDetails;
        this.leastDiscount = leastDiscount;
    }

    public int getLeastDiscount() {
        return leastDiscount;
    }

    public void setLeastDiscount(int leastDiscount) {
        this.leastDiscount = leastDiscount;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public List<PositionAutorityFrontDetail> getPositionAutorityFrontDetails() {
        return positionAutorityFrontDetails;
    }

    public void setPositionAutorityFrontDetails(List<PositionAutorityFrontDetail> positionAutorityFrontDetails) {
        this.positionAutorityFrontDetails = positionAutorityFrontDetails;
    }

    @Override
    public String toString() {
        return "Clerk{" +
                "position=" + position +
                ", shop=" + shop +
                ", positionAutorityFrontDetails=" + positionAutorityFrontDetails +
                ", leastDiscount=" + leastDiscount +
                '}';
    }
}

package com.yuyi.tea.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(value = { "handler" })
public class Clerk  extends User{

    private Position position;
    private Shop shop;
    private List<AuthorityDetail> authorities;

    public Clerk() {
    }

    public Clerk(int uid) {
        super(uid);
    }

    public Clerk(int uid, String name, String contact, String identityId, int gender, String address, Photo avatar, String password, Position position, Shop shop, List<AuthorityDetail> authorities) {
        super(uid, name, contact, identityId, gender, address, avatar, password);
        this.position = position;
        this.shop = shop;
        this.authorities = authorities;
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

    public List<AuthorityDetail> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<AuthorityDetail> authorities) {
        this.authorities = authorities;
    }

    @Override
    public String toString() {
        return "Clerk{" +
                "position=" + position +
                ", shop=" + shop +
                ", authorities=" + authorities +
                '}';
    }
}

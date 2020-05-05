package com.yuyi.tea.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(value = { "handler" })
public class ShopBox implements Serializable {

    private int uid;
    private String name;
    private String description;
    private Shop shop;
    private String boxNum;
    private Price price;
    private List<Photo> photos;
    private int duration;//每泡茶时间
    private boolean enforceTerminal=false;
    private List<Reservation> reservations;

    public ShopBox() {
    }

    public ShopBox(int uid) {
        this.uid = uid;
    }

    public ShopBox(int uid, String name, String description, Shop shop, String boxNum, Price price, List<Photo> photos, int duration, boolean enforceTerminal) {
        this.uid = uid;
        this.name = name;
        this.description = description;
        this.shop = shop;
        this.boxNum = boxNum;
        this.price = price;
        this.photos = photos;
        this.duration = duration;
        this.enforceTerminal = enforceTerminal;
    }

    @Override
    public String toString() {
        return "ShopBox{" +
                "uid=" + uid +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", shop=" + shop +
                ", boxNum='" + boxNum + '\'' +
                ", price=" + price +
                ", photos=" + photos +
                ", duration=" + duration +
                ", enforceTerminal=" + enforceTerminal +
                ", reservations=" + reservations +
                '}';
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public boolean isEnforceTerminal() {
        return enforceTerminal;
    }

    public void setEnforceTerminal(boolean enforceTerminal) {
        this.enforceTerminal = enforceTerminal;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public String getBoxNum() {
        return boxNum;
    }

    public void setBoxNum(String boxNum) {
        this.boxNum = boxNum;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }
}

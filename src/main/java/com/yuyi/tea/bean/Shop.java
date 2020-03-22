package com.yuyi.tea.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

/**
 * @JsonIgnoreProperties(value = { "handler" }) 防止在级联查询中因无对应数据的json转换而出错
 * 需要对所有级联类声明
 */
@JsonIgnoreProperties(value = { "handler" })
public class Shop implements Serializable {

    private int uid;
    private String name;
    private String address;
    private String description;
    private String contact;
    private List<OpenHour> openHours;
    private List<Photo> photos;
    private List<Clerk> clerks;
    private List<ShopBox> shopBoxes;

    public List<Photo> getPhotos() {
        return photos;
    }

    public Shop() {
    }

    public Shop(int uid) {
        this.uid = uid;
    }

    public Shop(int uid, String name, String address, String description, String contact, List<OpenHour> openHours, List<Photo> photos) {
        this.uid = uid;
        this.name = name;
        this.address = address;
        this.description = description;
        this.contact = contact;
        this.openHours = openHours;
        this.photos = photos;
    }

    public Shop(int uid, String name, String address, String description, String contact, List<OpenHour> openHours, List<Photo> photos, List<Clerk> clerks, List<ShopBox> shopBoxes) {
        this.uid = uid;
        this.name = name;
        this.address = address;
        this.description = description;
        this.contact = contact;
        this.openHours = openHours;
        this.photos = photos;
        this.clerks = clerks;
        this.shopBoxes = shopBoxes;
    }

    @Override
    public String toString() {
        return "Shop{" +
                "uid=" + uid +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", description='" + description + '\'' +
                ", contact='" + contact + '\'' +
                ", openHours=" + openHours +
                ", photos=" + photos +
                ", clerks=" + clerks +
                ", shopBoxes=" + shopBoxes +
                '}';
    }

    public List<ShopBox> getShopBoxes() {
        return shopBoxes;
    }

    public void setShopBoxes(List<ShopBox> shopBoxes) {
        this.shopBoxes = shopBoxes;
    }

    public List<Clerk> getClerks() {
        return clerks;
    }

    public void setClerks(List<Clerk> clerks) {
        this.clerks = clerks;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public List<OpenHour> getOpenHours() {
        return openHours;
    }

    public void setOpenHours(List<OpenHour> openHours) {
        this.openHours = openHours;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Shop(int uid, String name) {
        this.uid = uid;
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Shop(int uid, String name, String address, String description, String contact) {
        this.uid = uid;
        this.name = name;
        this.address = address;
        this.description = description;
        this.contact = contact;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
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

}

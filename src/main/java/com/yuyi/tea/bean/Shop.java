package com.yuyi.tea.bean;

import java.io.Serializable;
import java.util.List;

public class Shop implements Serializable {

    private int uid;
    private String name;
    private String address;
    private String description;
    private String contact;
    private List<OpenHour> openHours;
    private List<Photo> photos;

    public List<Photo> getPhotos() {
        return photos;
    }

    public Shop() {
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
                '}';
    }
}

package com.yuyi.tea.bean;

import java.io.Serializable;
import java.util.List;

public class Shop implements Serializable {

//    `uid` int(11) NOT NULL AUTO_INCREMENT,
//     `name` varchar(255) DEFAULT NULL,
//     `address` varchar(255) DEFAULT NULL,
//     `description` varchar(255) DEFAULT NULL,
//     `contact` varchar(20) DEFAULT NULL,

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

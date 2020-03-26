package com.yuyi.tea.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(value = { "handler" })
public class Product implements Serializable {

    private int uid;
    private String name;
    private ProductType type;
    private String description;
    private Price price;
    private int storage;
    private String status;
    private List<Photo> photos;
    private List<ActivityRule> activityRules=new ArrayList<>();

    public Product() {
    }

    public Product(int uid) {
        this.uid = uid;
    }

    public Product(int uid, String name, ProductType type) {
        this.uid = uid;
        this.name = name;
        this.type = type;
    }

    public Product(int uid, String name, ProductType type, String description, Price price, int storage, String status, List<Photo> photos, List<ActivityRule> activityRules) {
        this.uid = uid;
        this.name = name;
        this.type = type;
        this.description = description;
        this.price = price;
        this.storage = storage;
        this.status = status;
        this.photos = photos;
        this.activityRules = activityRules;
    }

    @Override
    public String toString() {
        return "Product{" +
                "uid=" + uid +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", storage=" + storage +
                ", status='" + status + '\'' +
                ", photos=" + photos +
                ", activityRules=" + activityRules +
                '}';
    }

    public List<ActivityRule> getActivityRules() {
        return activityRules;
    }

    public void setActivityRules(List<ActivityRule> activityRules) {
        this.activityRules = activityRules;
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

    public ProductType getType() {
        return type;
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public int getStorage() {
        return storage;
    }

    public void setStorage(int storage) {
        this.storage = storage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

}

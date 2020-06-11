package com.yuyi.tea.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.math.BigDecimal;
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
    private boolean enforceTerminal=false;
    private List<Photo> photos=new ArrayList<>();
    private List<ActivityRule> activityRules=new ArrayList<>();
    private List<Activity> activities=new ArrayList<>();
    private Shop shop;
    private boolean showOnHome=false;
    private BigDecimal sales=BigDecimal.valueOf(0);
    private List<Photo> productDetails=new ArrayList<>();

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

    public Product(int uid, String name, ProductType type, String description, Price price, int storage, boolean enforceTerminal, List<Photo> photos, List<ActivityRule> activityRules, List<Activity> activities, Shop shop, boolean showOnHome, BigDecimal sales, List<Photo> productDetails) {
        this.uid = uid;
        this.name = name;
        this.type = type;
        this.description = description;
        this.price = price;
        this.storage = storage;
        this.enforceTerminal = enforceTerminal;
        this.photos = photos;
        this.activityRules = activityRules;
        this.activities = activities;
        this.shop = shop;
        this.showOnHome = showOnHome;
        this.sales = sales;
        this.productDetails = productDetails;
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
                ", enforceTerminal=" + enforceTerminal +
                ", photos=" + photos +
                ", activityRules=" + activityRules +
                ", activities=" + activities +
                '}';
    }

    public List<Photo> getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(List<Photo> productDetails) {
        this.productDetails = productDetails;
    }

    public BigDecimal getSales() {
        return sales;
    }

    public void setSales(BigDecimal sales) {
        this.sales = sales;
    }

    public boolean isShowOnHome() {
        return showOnHome;
    }

    public void setShowOnHome(boolean showOnHome) {
        this.showOnHome = showOnHome;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
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

    public boolean isEnforceTerminal() {
        return enforceTerminal;
    }

    public void setEnforceTerminal(boolean enforceTerminal) {
        this.enforceTerminal = enforceTerminal;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

}

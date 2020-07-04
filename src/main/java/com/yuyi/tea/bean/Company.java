package com.yuyi.tea.bean;

import java.io.Serializable;

public class Company implements Serializable {

    private int uid;
    private String companyName;
    private String postCode;
    private String contact;
    private String websiteName;
    private String weChatOfficialAccount;
    private String address;
    private float rechargeRate;
    private float upgradeLimit;

    public Company() {
    }

    public Company(int uid) {
        this.uid = uid;
    }

    public Company(int uid, String companyName, String postCode, String contact, String websiteName, String weChatOfficialAccount, String address, float rechargeRate, float upgradeLimit) {
        this.uid = uid;
        this.companyName = companyName;
        this.postCode = postCode;
        this.contact = contact;
        this.websiteName = websiteName;
        this.weChatOfficialAccount = weChatOfficialAccount;
        this.address = address;
        this.rechargeRate = rechargeRate;
        this.upgradeLimit = upgradeLimit;
    }

    @Override
    public String toString() {
        return "Company{" +
                "uid=" + uid +
                ", companyName='" + companyName + '\'' +
                ", postCode='" + postCode + '\'' +
                ", contact='" + contact + '\'' +
                ", websiteName='" + websiteName + '\'' +
                ", weChatOfficialAccount='" + weChatOfficialAccount + '\'' +
                ", address='" + address + '\'' +
                ", rechargeRate=" + rechargeRate +
                '}';
    }

    public float getUpgradeLimit() {
        return upgradeLimit;
    }

    public void setUpgradeLimit(float upgradeLimit) {
        this.upgradeLimit = upgradeLimit;
    }

    public float getRechargeRate() {
        return rechargeRate;
    }

    public void setRechargeRate(float rechargeRate) {
        this.rechargeRate = rechargeRate;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getWebsiteName() {
        return websiteName;
    }

    public void setWebsiteName(String websiteName) {
        this.websiteName = websiteName;
    }

    public String getWeChatOfficialAccount() {
        return weChatOfficialAccount;
    }

    public void setWeChatOfficialAccount(String weChatOfficialAccount) {
        this.weChatOfficialAccount = weChatOfficialAccount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

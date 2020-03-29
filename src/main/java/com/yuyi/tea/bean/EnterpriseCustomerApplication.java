package com.yuyi.tea.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(value = { "handler" })
public class EnterpriseCustomerApplication implements Serializable {

    private int uid;
    private long applyTime;
    private Enterprise enterprise;
    private Customer applicant;
    private String status="submit";

    public EnterpriseCustomerApplication() {
    }

    public EnterpriseCustomerApplication(int uid) {
        this.uid = uid;
    }

    public EnterpriseCustomerApplication(int uid, long applyTime, Enterprise enterprise, Customer applicant, String status) {
        this.uid = uid;
        this.applyTime = applyTime;
        this.enterprise = enterprise;
        this.applicant = applicant;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public long getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(long applyTime) {
        this.applyTime = applyTime;
    }

    public Enterprise getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(Enterprise enterprise) {
        this.enterprise = enterprise;
    }

    public Customer getApplicant() {
        return applicant;
    }

    public void setApplicant(Customer applicant) {
        this.applicant = applicant;
    }

    @Override
    public String toString() {
        return "EnterpriseCustomerApplication{" +
                "uid=" + uid +
                ", applyTime=" + applyTime +
                ", enterprise=" + enterprise +
                ", applicant=" + applicant +
                ", status='" + status + '\'' +
                '}';
    }
}

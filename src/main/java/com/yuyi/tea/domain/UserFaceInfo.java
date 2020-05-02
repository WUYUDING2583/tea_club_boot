package com.yuyi.tea.domain;


import com.yuyi.tea.bean.Clerk;
import com.yuyi.tea.bean.Customer;

import java.util.Date;


public class UserFaceInfo {

        private Integer id;

        private Integer groupId;

        private String faceId;

        private Date createTime;

        private Date updateTime;

        private byte[] faceFeature;

        private byte[] face;

        private Customer customer=null;

        private Clerk clerk=null;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

    public byte[] getFace() {
        return face;
    }

    public void setFace(byte[] face) {
        this.face = face;
    }

    public Integer getGroupId() {
            return groupId;
        }

        public void setGroupId(Integer groupId) {
            this.groupId = groupId;
        }

        public String getFaceId() {
            return faceId;
        }

        public void setFaceId(String faceId) {
            this.faceId = faceId;
        }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Clerk getClerk() {
        return clerk;
    }

    public void setClerk(Clerk clerk) {
        this.clerk = clerk;
    }

    public Date getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Date createTime) {
            this.createTime = createTime;
        }

        public Date getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Date updateTime) {
            this.updateTime = updateTime;
        }

        public byte[] getFaceFeature() {
            return faceFeature;
        }

        public void setFaceFeature(byte[] faceFeature) {
            this.faceFeature = faceFeature;
        }
    }


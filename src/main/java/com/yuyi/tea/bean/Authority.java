package com.yuyi.tea.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(value = { "handler" })
public class Authority implements Serializable {

    private int uid;
    private String name;
    private String title;
    private String icon;
    private List<AuthorityFront> details;

    public Authority() {
    }

    public Authority(int uid) {
        this.uid = uid;
    }

    public Authority(int uid, String name, String title, String icon, List<AuthorityFront> details) {
        this.uid = uid;
        this.name = name;
        this.title = title;
        this.icon = icon;
        this.details = details;
    }

    public List<AuthorityFront> getDetails() {
        return details;
    }

    public void setDetails(List<AuthorityFront> details) {
        this.details = details;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "Authority{" +
                "uid=" + uid +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", icon='" + icon + '\'' +
                ", details=" + details +
                '}';
    }
}


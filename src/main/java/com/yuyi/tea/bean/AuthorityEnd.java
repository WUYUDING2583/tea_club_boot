package com.yuyi.tea.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(value = { "handler" })
public class AuthorityEnd implements Serializable {

    private int uid;
    private String url;
    private String description;
    private AuthorityFront belongFront;
    private String method;

    public AuthorityEnd() {
    }

    public AuthorityEnd(int uid) {
        this.uid = uid;
    }

    public AuthorityEnd(int uid, String url, String description, AuthorityFront belongFront, String method) {
        this.uid = uid;
        this.url = url;
        this.description = description;
        this.belongFront = belongFront;
        this.method = method;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AuthorityFront getBelongFront() {
        return belongFront;
    }

    public void setBelongFront(AuthorityFront belongFront) {
        this.belongFront = belongFront;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return "AuthorityEnd{" +
                "uid=" + uid +
                ", url='" + url + '\'' +
                ", description='" + description + '\'' +
                ", belongFront=" + belongFront +
                ", method='" + method + '\'' +
                '}';
    }
}

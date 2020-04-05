package com.yuyi.tea.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;


@JsonIgnoreProperties(value = { "handler" })
public class AuthorityFront implements Serializable {

    private int uid;
    private String name;
    private String component;
    private String title;
    private String description;
    private Authority belong;
    private List<AuthorityEnd> authorityEnds;
    private boolean auth=true;

    public AuthorityFront() {
    }

    public AuthorityFront(int uid) {
        this.uid = uid;
    }

    public AuthorityFront(int uid, String name, String component, String title, String description, Authority belong, List<AuthorityEnd> authorityEnds, boolean auth) {
        this.uid = uid;
        this.name = name;
        this.component = component;
        this.title = title;
        this.description = description;
        this.belong = belong;
        this.authorityEnds = authorityEnds;
        this.auth = auth;
    }

    public List<AuthorityEnd> getAuthorityEnds() {
        return authorityEnds;
    }

    public void setAuthorityEnds(List<AuthorityEnd> authorityEnds) {
        this.authorityEnds = authorityEnds;
    }

    public boolean isAuth() {
        return auth;
    }

    public void setAuth(boolean auth) {
        this.auth = auth;
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

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Authority getBelong() {
        return belong;
    }

    public void setBelong(Authority belong) {
        this.belong = belong;
    }

    @Override
    public String toString() {
        return "AuthorityFront{" +
                "uid=" + uid +
                ", name='" + name + '\'' +
                ", component='" + component + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", belong=" + belong +
                ", authorityEnds=" + authorityEnds +
                ", auth=" + auth +
                '}';
    }
}

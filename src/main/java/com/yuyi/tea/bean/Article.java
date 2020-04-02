package com.yuyi.tea.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(value = { "handler" })
public class Article implements Serializable {

    private int uid;
    private String name;
    private String url;
    private List<Tag> tags;
    private Photo photo;
    private long time;
    private boolean enforceTerminal=false;

    public Article() {
    }

    public Article(int uid) {
        this.uid = uid;
    }

    public Article(int uid, String name, String url, List<Tag> tags, Photo photo, long time, boolean enforceTerminal) {
        this.uid = uid;
        this.name = name;
        this.url = url;
        this.tags = tags;
        this.photo = photo;
        this.time = time;
        this.enforceTerminal = enforceTerminal;
    }

    public boolean isEnforceTerminal() {
        return enforceTerminal;
    }

    public void setEnforceTerminal(boolean enforceTerminal) {
        this.enforceTerminal = enforceTerminal;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Article{" +
                "uid=" + uid +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", tags=" + tags +
                ", photo=" + photo +
                ", time=" + time +
                ", enforceTerminal=" + enforceTerminal +
                '}';
    }
}

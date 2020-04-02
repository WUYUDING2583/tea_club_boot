package com.yuyi.tea.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * 文章标签类
 */
@JsonIgnoreProperties(value = { "handler" })
public class Tag implements Serializable {

    private int uid;
    private String name;

    public Tag() {
    }

    public Tag(int uid) {
        this.uid = uid;
    }

    public Tag(int uid, String name) {
        this.uid = uid;
        this.name = name;
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
        return "Tag{" +
                "uid=" + uid +
                ", name='" + name + '\'' +
                '}';
    }
}

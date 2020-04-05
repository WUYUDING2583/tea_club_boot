package com.yuyi.tea.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(value = { "handler" })
public class PositionAutorityEndDetail implements Serializable {

    private int uid;
    private Position position;
    private AuthorityEnd authorityEnd;

    public PositionAutorityEndDetail() {
    }

    public PositionAutorityEndDetail(int uid) {
        this.uid = uid;
    }

    public PositionAutorityEndDetail(int uid, Position position, AuthorityEnd authorityEnd) {
        this.uid = uid;
        this.position = position;
        this.authorityEnd = authorityEnd;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public AuthorityEnd getAuthorityEnd() {
        return authorityEnd;
    }

    public void setAuthorityEnd(AuthorityEnd authorityEnd) {
        this.authorityEnd = authorityEnd;
    }

    @Override
    public String toString() {
        return "PositionAutorityEndDetail{" +
                "uid=" + uid +
                ", position=" + position +
                ", authorityEnd=" + authorityEnd +
                '}';
    }
}

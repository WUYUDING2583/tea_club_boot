package com.yuyi.tea.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(value = { "handler" })
public class PositionAutorityFrontDetail implements Serializable {

    private int uid;
    private Position position;
    private AuthorityFront authorityFront;

    public PositionAutorityFrontDetail() {
    }

    public PositionAutorityFrontDetail(int uid) {
        this.uid = uid;
    }

    public PositionAutorityFrontDetail(int uid, Position position, AuthorityFront authorityFront) {
        this.uid = uid;
        this.position = position;
        this.authorityFront = authorityFront;
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

    public AuthorityFront getAuthorityFront() {
        return authorityFront;
    }

    public void setAuthorityFront(AuthorityFront authorityFront) {
        this.authorityFront = authorityFront;
    }

    @Override
    public String toString() {
        return "PositionAutorityFrontDetail{" +
                "uid=" + uid +
                ", position=" + position +
                ", authorityFront=" + authorityFront +
                '}';
    }
}

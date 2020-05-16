package com.yuyi.tea.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(value = { "handler" })
public class Activity implements Serializable {

    private int uid;
    private String name;
    private String description;
    private long startTime;
    private long endTime;
    private boolean enforceTerminal;
    private List<Photo> photos;
    private List<Activity> mutexActivities;
    private List<ActivityRule> activityRules;
    private int priority;
    private boolean isShowOnHome=false;

    public Activity() {
    }

    public Activity(int uid) {
        this.uid = uid;
    }

    public Activity(int uid, String name) {
        this.uid = uid;
        this.name = name;
    }

    public Activity(int uid, String name, String description, long startTime, long endTime, boolean enforceTerminal, List<Photo> photos, List<Activity> mutexActivities, List<ActivityRule> activityRules, int priority, boolean isShowOnHome) {
        this.uid = uid;
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.enforceTerminal = enforceTerminal;
        this.photos = photos;
        this.mutexActivities = mutexActivities;
        this.activityRules = activityRules;
        this.priority = priority;
        this.isShowOnHome = isShowOnHome;
    }

    public boolean isShowOnHome() {
        return isShowOnHome;
    }

    public void setShowOnHome(boolean showOnHome) {
        isShowOnHome = showOnHome;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public boolean isEnforceTerminal() {
        return enforceTerminal;
    }

    public void setEnforceTerminal(boolean enforceTerminal) {
        this.enforceTerminal = enforceTerminal;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public List<Activity> getMutexActivities() {
        return mutexActivities;
    }

    public void setMutexActivities(List<Activity> mutexActivities) {
        this.mutexActivities = mutexActivities;
    }

    public List<ActivityRule> getActivityRules() {
        return activityRules;
    }

    public void setActivityRules(List<ActivityRule> activityRules) {
        this.activityRules = activityRules;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "uid=" + uid +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", enforceTerminal=" + enforceTerminal +
                ", photos=" + photos +
                ", mutexActivities=" + mutexActivities +
                ", activityRules=" + activityRules +
                '}';
    }
}

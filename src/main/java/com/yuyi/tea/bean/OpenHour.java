package com.yuyi.tea.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 门店营业时间类
 */
public class OpenHour implements Serializable {

    private String startTime;
    private String endTime;

    @Override
    public String toString() {
        return "OpenHour{" +
                "startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", repeat=" + repeat +
                ", shopId=" + shopId +
                '}';
    }

    private List<Integer> repeat;
    private int shopId;

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public OpenHour(String startTime, String endTime, List<Integer> repeat) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.repeat = repeat;
    }

    public OpenHour() {
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public List<Integer> getRepeat() {
        return repeat;
    }

    public void setRepeat(List<Integer> repeat) {
        this.repeat = repeat;
    }
}

package com.yuyi.tea.component;

import java.io.Serializable;

public class TimeRange implements Serializable {

    private long startDate;
    private long endDate;

    public TimeRange() {
    }

    public TimeRange(long startDate, long endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "TimeRange{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}

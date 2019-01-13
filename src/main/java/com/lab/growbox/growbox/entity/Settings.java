package com.lab.growbox.growbox.entity;

import java.sql.Date;
import java.sql.Time;

public class Settings {
    private int period;
    private Time wtTime;
    private Time cTime;
    private Date cDate;

    public Settings(int period, Time wtTime, Time cTime, Date cDate) {
        this.period = period;
        this.wtTime = wtTime;
        this.cTime = cTime;
        this.cDate = cDate;
    }

    public Settings() {
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public Time getWtTime() {
        return wtTime;
    }

    public void setWtTime(Time wtTime) {
        this.wtTime = wtTime;
    }

    public Time getcTime() {
        return cTime;
    }

    public void setcTime(Time cTime) {
        this.cTime = cTime;
    }

    public Date getcDate() {
        return cDate;
    }

    public void setcDate(Date cDate) {
        this.cDate = cDate;
    }

    @Override
    public String toString() {
        return "Settings{" +
                "period=" + period +
                ", wtTime=" + wtTime +
                ", cTime=" + cTime +
                ", cDate=" + cDate +
                '}';
    }
}

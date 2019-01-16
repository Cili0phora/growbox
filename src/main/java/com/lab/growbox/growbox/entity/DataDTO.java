package com.lab.growbox.growbox.entity;

import java.sql.Date;
import java.sql.Time;

public class DataDTO {
    private Date date;
    private Time time;
    private float temperature;
    private int groundHum;
    private float airHum;
    private int waterLevel;
    private float bright;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public int getGroundHum() {
        return groundHum;
    }

    public void setGroundHum(int groundHum) {
        this.groundHum = groundHum;
    }

    public float getAirHum() {
        return airHum;
    }

    public void setAirHum(float airHum) {
        this.airHum = airHum;
    }

    public int getWaterLevel() {
        return waterLevel;
    }

    public void setWaterLevel(int waterLevel) {
        this.waterLevel = waterLevel;
    }

    public float getBright() {
        return bright;
    }

    public void setBright(float bright) {
        this.bright = bright;
    }

    @Override
    public String toString() {
        return "DataDTO{" +
                "date=" + date +
                ", time=" + time +
                ", temperature=" + temperature +
                ", groundHum=" + groundHum +
                ", airHum=" + airHum +
                ", waterLevel=" + waterLevel +
                ", bright=" + bright +
                '}';
    }
}

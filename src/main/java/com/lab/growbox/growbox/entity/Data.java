package com.lab.growbox.growbox.entity;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Date;
import java.sql.Time;

@Entity
public class Data {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
    private Date date;
    private long time;
    private float temperature;
    private int groundHum;
    private float airHum;
    private int waterLevel;
    private float bright;

    public Data() {
    }

    public Data(Date date, long time, float temperature, int groundHum, float airHum, int waterLevel, float bright) {
        this.date = date;
        this.time = time;
        this.temperature = temperature;
        this.groundHum = groundHum;
        this.airHum = airHum;
        this.waterLevel = waterLevel;
        this.bright = bright;
    }

    public Data(int id, Date date, long time, float temperature, int groundHum, float airHum, int waterLevel, float bright) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.temperature = temperature;
        this.groundHum = groundHum;
        this.airHum = airHum;
        this.waterLevel = waterLevel;
        this.bright = bright;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
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
        return "Data{" +
                "id=" + id +
                ", date=" + date +
                ", time=" + time +
                ", temperature=" + temperature +
                ", groundHum=" + groundHum +
                ", airHum=" + airHum +
                ", waterLevel=" + waterLevel +
                ", bright=" + bright +
                '}';
    }
}

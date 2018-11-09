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
    private Time time;
    private float temperature;
    private int groundHum;
    private int airHum;
    private int waterLevel;

    public Data(int id, Date date, Time time, float temperature, int groundHum, int airHum, int waterLevel) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.temperature = temperature;
        this.groundHum = groundHum;
        this.airHum = airHum;
        this.waterLevel = waterLevel;
    }

    public Data(Date date, Time time, float temperature, int groundHum, int airHum, int waterLevel) {
        this.date = date;
        this.time = time;
        this.temperature = temperature;
        this.groundHum = groundHum;
        this.airHum = airHum;
        this.waterLevel = waterLevel;
    }

    public Data() {
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

    public int getAirHum() {
        return airHum;
    }

    public void setAirHum(int airHum) {
        this.airHum = airHum;
    }

    public int getGroundHum() {
        return groundHum;
    }

    public void setGroundHum(int groundHum) {
        this.groundHum = groundHum;
    }

    public int getWaterLevel() {
        return waterLevel;
    }

    public void setWaterLevel(int waterLevel) {
        this.waterLevel = waterLevel;
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
                '}';
    }
}

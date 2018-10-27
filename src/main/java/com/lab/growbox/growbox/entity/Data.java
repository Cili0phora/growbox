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
    private int grountHum;
    private int airHum;

    public Data(int id, Date date, Time time, float temperature, int grountHum, int airHum) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.temperature = temperature;
        this.grountHum = grountHum;
        this.airHum = airHum;
    }

    public Data(Date date, Time time, float temperature, int grountHum, int airHum) {
        this.date = date;
        this.time = time;
        this.temperature = temperature;
        this.grountHum = grountHum;
        this.airHum = airHum;
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

    public int getGrountHum() {
        return grountHum;
    }

    public void setGrountHum(int grountHum) {
        this.grountHum = grountHum;
    }

    public int getAirHum() {
        return airHum;
    }

    public void setAirHum(int airHum) {
        this.airHum = airHum;
    }
}

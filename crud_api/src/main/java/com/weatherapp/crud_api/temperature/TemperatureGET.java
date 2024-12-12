package com.weatherapp.crud_api.temperature;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TemperatureGET {
    private int id;

    private double temperature;

    private String date;

    public TemperatureGET(int id, double temperature, String date) {
        this.id = id;
        this.temperature = temperature;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

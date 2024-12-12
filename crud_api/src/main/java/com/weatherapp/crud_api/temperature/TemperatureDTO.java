package com.weatherapp.crud_api.temperature;

public class TemperatureDTO {
    private int idOras;
    private double valoare; // Represents the temperature value

    // Getters and Setters
    public int getIdOras() {
        return idOras;
    }

    public void setIdOras(int idOras) {
        this.idOras = idOras;
    }

    public double getValoare() {
        return valoare;
    }

    public void setValoare(double valoare) {
        this.valoare = valoare;
    }
}


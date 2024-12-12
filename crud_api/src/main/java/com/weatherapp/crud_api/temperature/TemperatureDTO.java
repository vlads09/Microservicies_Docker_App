package com.weatherapp.crud_api.temperature;

public class TemperatureDTO {
    private Integer idOras;
    private Double valoare; // Represents the temperature value

    // Getters and Setters
    public Integer getIdOras() {
        return idOras;
    }

    public void setIdOras(int idOras) {
        this.idOras = idOras;
    }

    public Double getValoare() {
        return valoare;
    }

    public void setValoare(double valoare) {
        this.valoare = valoare;
    }
}


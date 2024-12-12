package com.weatherapp.crud_api.temperature;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "Temperature",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"id_oras", "timestamp"})
        }
)
public class Temperature {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private double temperature;

    private LocalDateTime timestamp;

    private int idOras;

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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getId_oras() {
        return idOras;
    }

    public void setId_oras(int id_oras) {
        this.idOras = id_oras;
    }
}

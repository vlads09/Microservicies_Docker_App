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
    private Integer id;

    private Double temperature;

    private LocalDateTime timestamp;

    private Integer idOras;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getIdOras() {
        return idOras;
    }

    public void setIdOras(Integer idOras) {
        this.idOras = idOras;
    }
}

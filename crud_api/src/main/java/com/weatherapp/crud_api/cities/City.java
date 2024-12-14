package com.weatherapp.crud_api.cities;

import jakarta.persistence.*;

@Entity
@Table(
        name = "City",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"nume", "idTara"})
        }
)
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Integer idTara;

    private String nume;

    private Double lat;
    private Double lon;

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdTara() {
        return idTara;
    }

    public void setIdTara(Integer idTara) {
        this.idTara = idTara;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }
}

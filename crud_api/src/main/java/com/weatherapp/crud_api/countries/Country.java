package com.weatherapp.crud_api.countries;

import jakarta.persistence.*;

@Entity
@Table(name = "Country", uniqueConstraints = @UniqueConstraint(columnNames = "nume"))
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "nume", unique = true, nullable = false)
    private String nume;

    private Double lat;
    private Double lon;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
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

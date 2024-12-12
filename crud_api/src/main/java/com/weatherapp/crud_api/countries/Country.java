package com.weatherapp.crud_api.countries;

import jakarta.persistence.*;

@Entity
@Table(name = "Country", uniqueConstraints = @UniqueConstraint(columnNames = "nume"))
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "nume", unique = true, nullable = false)
    private String nume;

    private Double lat;
    private Double lon;

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}

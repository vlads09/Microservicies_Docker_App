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
    private int id;

    @Column(name = "idTara", unique = true, nullable = false)
    private int idTara;

    @Column(name = "nume", unique = true, nullable = false)
    private String nume;

    private double lat;
    private double lon;

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

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public int getIdTara() {
        return idTara;
    }

    public void setIdTara(int idTara) {
        this.idTara = idTara;
    }
}

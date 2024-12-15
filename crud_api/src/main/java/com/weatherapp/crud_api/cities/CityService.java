package com.weatherapp.crud_api.cities;

import com.weatherapp.crud_api.countries.CountryRepository;
import com.weatherapp.crud_api.temperature.TemperatureRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CityService {

    // Injecting the required dependency, eliminating the need for manual instantiation or configuration
    @Autowired
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    @Autowired
    private TemperatureRepository temperatureRepository;

    public CityService(CityRepository cityRepository, CountryRepository countryRepository) {
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
    }

    public ResponseEntity<Object> newCity(City city) {
        // Check if the request was made correctly
        // By checking the city name and if the country id exists in the db
        if (city.getNume() == null || countryRepository.findById(city.getIdTara()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // Check if a city with this name exists in the same country
        Optional<City> existingCity = cityRepository.findByNume(city.getNume());
        if (existingCity.isPresent() && existingCity.get().getIdTara() == city.getIdTara()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        // Save the new city
        City savedCity = cityRepository.save(city);

        // Return 201 Created with ID
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("{\"id\": " + savedCity.getId() + "}");
    }

    public ResponseEntity<Object> getCities() {
        Iterable<City> cities = cityRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(cities);
    }

    public ResponseEntity<Object> getCitiesByCountry(Integer id) {
        // In order to get the cities from a certain country
        // Check if the country exists in the db
        if (countryRepository.findById(id).isPresent()) {
            Iterable<City> cities = cityRepository.findAllByIdTara(id);
            return ResponseEntity.status(HttpStatus.OK).body(cities);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
        }
    }

    public ResponseEntity<Object> getCitiesByCountry2() {
        // Corner-Case if there is no country id provided in the path
        // Return an empty list
        return ResponseEntity.status(HttpStatus.OK).body("[]");
    }

    public ResponseEntity<Object> updateCity(City city, Integer id) {
        // Check if the request was made correctly
        if (city.getId() == null || city.getNume() == null || city.getIdTara() == null
                || city.getLon() == null || city.getLat() == null || !city.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // Check if the city id and country id exist in the db
        if (cityRepository.findById(id).isPresent() && countryRepository.findById(city.getIdTara()).isPresent()) {
            Optional<City> existingCity = cityRepository.findById(id);
            // Check if the request can update
            // Return conflict if:
            // 1. The name in the request and the city to be modified are different
            // 2. There is a city with the same name other than the one to be modified
            // 3. Country id provided is the same with the id in the found city at 2.
            if (!city.getNume().equals(existingCity.get().getNume())
                    && cityRepository.findByNume(city.getNume()).isPresent()
                    && cityRepository.findByNume(city.getNume()).get().getIdTara().equals(city.getIdTara())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("");
            }

            // Update
            existingCity.get().setNume(city.getNume());
            existingCity.get().setLat(city.getLat());
            existingCity.get().setLon(city.getLon());
            cityRepository.save(existingCity.get());
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Transactional
    public ResponseEntity<Object> deleteCity(Integer id) {
        if (cityRepository.findById(id).isPresent()) {
            // Delete the temperatures related to the city
            temperatureRepository.deleteAllByIdOras(id);
            cityRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

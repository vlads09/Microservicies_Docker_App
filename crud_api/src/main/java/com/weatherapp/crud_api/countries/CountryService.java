package com.weatherapp.crud_api.countries;

import com.weatherapp.crud_api.cities.City;
import com.weatherapp.crud_api.cities.CityRepository;
import com.weatherapp.crud_api.temperature.TemperatureRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CountryService {

    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    private final TemperatureRepository temperatureRepository;

    @Autowired
    public CountryService(CountryRepository countryRepository, CityRepository cityRepository, TemperatureRepository temperatureRepository) {
        this.countryRepository = countryRepository;
        this.cityRepository = cityRepository;
        this.temperatureRepository = temperatureRepository;
    }

    public ResponseEntity<Object> newCountry(Country country) {
        // Check if the request was made correctly
        if (country.getNume() == null || country.getLat() == null || country.getLon() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // Check if a country with the same name already exists
        if (countryRepository.findByNume(country.getNume()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("");
        }

        // Save the new country
        Country savedCountry = countryRepository.save(country);

        // Return 201 Created with ID
        return ResponseEntity.status(HttpStatus.CREATED).body("{\"id\": " + savedCountry.getId() + "}");
    }

    public ResponseEntity<Object> getCountries() {
        Iterable<Country> countries = countryRepository.findAll();
        return new ResponseEntity<>(countries, HttpStatus.OK);
    }

    public ResponseEntity<Object> updateCountry(Country country, Integer id) {
        // Check if the request was made correctly
        if (country.getId() == null || country.getNume() == null
                || country.getLat() == null
                || country.getLon() == null
                || !country.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // Check if the country with the id provided exists in the db
        if (countryRepository.findById(id).isPresent()) {
            Optional<Country> existingCountry = countryRepository.findById(id);

            // Check if the request can update
            // Return conflict if:
            // 1. The name in the request and the country to be modified are different
            // 2. There is a country with the same name other than the one to be modified
            if (!country.getNume().equals(existingCountry.get().getNume())
                    && countryRepository.findByNume(country.getNume()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("");
            }

            // Update
            existingCountry.get().setNume(country.getNume());
            existingCountry.get().setLat(country.getLat());
            existingCountry.get().setLon(country.getLon());
            countryRepository.save(existingCountry.get());
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Transactional
    public ResponseEntity<Object> deleteCountry(Integer id) {
        if (countryRepository.findById(id).isPresent()) {
            // Find all the cities from the country
            // And delete them and their temperatures
            Iterable<City> cities = cityRepository.findAllByIdTara(id);
            for (City city : cities) {
                temperatureRepository.deleteAllByIdOras(city.getId());
                cityRepository.deleteById(city.getId());
            }
            countryRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

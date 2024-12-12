package com.weatherapp.crud_api.cities;

import com.weatherapp.crud_api.countries.Country;
import com.weatherapp.crud_api.countries.CountryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
public class CityService {

    @Autowired
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;

    public CityService(CityRepository cityRepository, CountryRepository countryRepository) {
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
    }

    public ResponseEntity<Object> newCity(City city) {
        // Check if city is null
        if (city.getNume() == null || countryRepository.findById(city.getIdTara()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // Check if a country with the same name already exists
        if (cityRepository.findByNume(city.getNume()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("");
        }

        // Save the new city
        City savedCity = cityRepository.save(city);

        // Return 201 Created with ID
        return ResponseEntity.status(HttpStatus.CREATED).body("{\"id\": " + savedCity.getId() + "}");
    }

    public ResponseEntity<Object> getCities() {
        Iterable<City> cities = cityRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(cities);
    }

    public ResponseEntity<Object> getCitiesByCountry(Integer id) {
        if (countryRepository.findById(id).isPresent()) {
            Iterable<City> cities = cityRepository.findAllByIdTara(id);
            return ResponseEntity.status(HttpStatus.OK).body(cities);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
        }
    }

    public ResponseEntity<Object> getCitiesByCountry2() {
        return ResponseEntity.status(HttpStatus.OK).body("[]");
    }

    public ResponseEntity<Object> updateCity(City city, Integer id) {
        // Check if the id exists
        if (city.getId() == 0 || city.getNume() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (cityRepository.findById(id).isPresent() && countryRepository.findById(city.getIdTara()).isPresent()) {
            Optional<City> existingCity = cityRepository.findById(id);
            if (!city.getNume().equals(existingCity.get().getNume()) &&
                    cityRepository.findByNume(city.getNume()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("");
            }
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
            cityRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

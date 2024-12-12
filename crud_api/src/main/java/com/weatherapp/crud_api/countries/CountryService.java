package com.weatherapp.crud_api.countries;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CountryService {

    private final CountryRepository countryRepository;

    @Autowired
    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public ResponseEntity<Object> newCountry(Country country) {
        // Check if country is null
        if (country.getNume() == null) {
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
        // Check if the id exists
        if (country.getId() == 0 || country.getNume() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if (countryRepository.findById(id).isPresent()) {
            Optional<Country> existingCountry = countryRepository.findById(id);
            if (!country.getNume().equals(existingCountry.get().getNume()) &&
                    countryRepository.findByNume(existingCountry.get().getNume()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("");
            }
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
            countryRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

package com.weatherapp.crud_api.countries;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/countries")
public class CountryController {
    private final CountryService countryService;

    @Autowired
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @PostMapping("")
    public ResponseEntity<Object> addCountry(@RequestBody Country country) {
        return countryService.newCountry(country);
    }

    @GetMapping("")
    public ResponseEntity<Object> getAllCountries() {
        return countryService.getCountries();
    }


    @PutMapping("/{id}")
    public ResponseEntity<Object> updateCountry(@RequestBody Country country, @PathVariable Integer id) {
        return countryService.updateCountry(country, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCountry(@PathVariable Integer id) {
        return countryService.deleteCountry(id);
    }

}

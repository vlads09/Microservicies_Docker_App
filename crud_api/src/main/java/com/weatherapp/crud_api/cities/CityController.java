package com.weatherapp.crud_api.cities;

import com.weatherapp.crud_api.countries.Country;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cities")
public class CityController {

    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @PostMapping("")
    public ResponseEntity<Object> addCity(@RequestBody City city) {
        return cityService.newCity(city);
    }

    @GetMapping("")
    public ResponseEntity<Object> getAllCities() {
        return cityService.getCities();
    }

    @GetMapping("/country/{id}")
    public ResponseEntity<Object> getAllCitiesByCountry(@PathVariable Integer id) {
        return cityService.getCitiesByCountry(id);
    }

    @GetMapping("/country/")
    public ResponseEntity<Object> getAllCitiesByCountry2() {
        return cityService.getCitiesByCountry2();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateCity(@RequestBody City city, @PathVariable Integer id) {
        return cityService.updateCity(city, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCity(@PathVariable Integer id) {
        return cityService.deleteCity(id);
    }
}

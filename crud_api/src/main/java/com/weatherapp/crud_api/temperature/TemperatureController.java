package com.weatherapp.crud_api.temperature;


import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/temperatures")
public class TemperatureController {

    private final TemperatureService temperatureService;
    public TemperatureController(TemperatureService temperatureService) {
        this.temperatureService = temperatureService;
    }

    @PostMapping("")
    public ResponseEntity<Object> addTemperature(@RequestBody TemperaturePUT temperature) {
        return temperatureService.addTemperature(temperature);
    }

    @GetMapping("")
    public ResponseEntity<Object> getAllTemperaturesByConditions(@RequestParam(required = false) Double lat,
                                                                 @RequestParam(required = false) Double lon,
                                                                 @RequestParam(required = false) String from,
                                                                 @RequestParam(required = false) String until) {
        return temperatureService.getTemperatureByConditions(new TemperatureParams(lat, lon, from, until));
    }

    @GetMapping("/cities/{id}")
    public ResponseEntity<Object> getAllTemperaturesCityByDate(@RequestParam(required = false) String from,
                                                               @RequestParam(required = false) String until,
                                                               @PathVariable Integer id) {
        return temperatureService.getTemperaturesCityByDate(new TemperatureParams(null, null, from, until),
                 id);
    }

    @GetMapping("/cities/")
    public ResponseEntity<Object> getAllTemperaturesCityByDate2() {
        return temperatureService.getTemperaturesCityByDate2();
    }

    @GetMapping("/countries/{id}")
    public ResponseEntity<Object> getAllTemperaturesByCountry(@PathVariable Integer id) {
        return temperatureService.getAllTemperaturesByCountry(id);
    }

    @GetMapping("/countries/")
    public ResponseEntity<Object> getAllTemperaturesByCountry2() {
        return temperatureService.getAllTemperaturesByCountry2();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTemperature(@PathVariable Integer id, @RequestBody TemperatureDTO temperature) {
        return temperatureService.updateTemperature(temperature, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTemperature(@PathVariable Integer id) {
        return temperatureService.deleteTemperature(id);
    }
}

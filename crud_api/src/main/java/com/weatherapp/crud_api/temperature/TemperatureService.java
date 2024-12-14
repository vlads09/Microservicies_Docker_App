package com.weatherapp.crud_api.temperature;

import com.weatherapp.crud_api.cities.CityRepository;
import com.weatherapp.crud_api.countries.CountryRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TemperatureService {
    private final TemperatureRepository temperatureRepository;
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;

    public TemperatureService(TemperatureRepository temperatureRepository, CityRepository cityRepository, CountryRepository countryRepository) {
        this.temperatureRepository = temperatureRepository;
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
    }

    public ResponseEntity<Object> addTemperature(TemperatureDTO temperature) {
        // check if the request was made correctly
        if (temperature.getIdOras() == null || temperature.getValoare() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 400
        }

        // check if the city exists in the db
        if (cityRepository.existsById(temperature.getIdOras())) {
            // if the request was submitted at the same time with another exactly the same
            if (temperatureRepository.existsByTimestamp(LocalDateTime.now())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409
            }

            // Save the temperature
            Temperature temp = new Temperature();
            temp.setIdOras(temperature.getIdOras());
            temp.setTemperature(temperature.getValoare());
            temp.setTimestamp(LocalDateTime.now());
            temperatureRepository.save(temp);
            return ResponseEntity.status(HttpStatus.CREATED).body("{\"id\": " + temp.getId() + "}");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
        }
    }

    public ResponseEntity<Object> updateTemperature(TemperaturePUT temperature, Integer id) {
        // Check if the request was made correctly
        if (id == null || temperature.getIdOras() == null || temperature.getValoare() == null
                || !temperatureRepository.existsById(id) || !id.equals(temperature.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 400
        }

        // Check if the city id exists
        if (cityRepository.existsById(temperature.getIdOras())) {
            Optional<Temperature> existingTemp = temperatureRepository.findById(id);

            // Update
            existingTemp.get().setTemperature(temperature.getValoare());
            temperatureRepository.save(existingTemp.get());
            return ResponseEntity.status(HttpStatus.OK).build();
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    public ResponseEntity<Object> getTemperatureByConditions(TemperatureParams params) {
        // If there are no parameters sent, then return an empty list
        if (params.isNull()) {
            return ResponseEntity.status(HttpStatus.OK).body("[]");
        }

        List<Integer> citiesIds;
        LocalDateTime from = null;
        LocalDateTime until = null;
        // Set the times so they correspond with the timestamp in database
        if (params.getFrom() != null) {
            from = LocalDateTime.parse(params.getFrom() + "T00:00:00");
        }
        if (params.getUntil() != null) {
            until = LocalDateTime.parse(params.getUntil() + "T23:59:59");
        }
        // Return the temperatures based on which parameters were provided
        return switch (params.whichCase()) {
            case NULL_BESIDES_LATITUDE -> {
                citiesIds = cityRepository.findIdsByLat(params.getLatitude());
                yield ResponseEntity.status(HttpStatus.OK).body(temperatureRepository.findAllByCityIds(citiesIds));
            }
            case NULL_BESIDES_LONGITUDE -> {
                citiesIds = cityRepository.findIdsByLon(params.getLongitude());
                yield ResponseEntity.status(HttpStatus.OK).body(temperatureRepository.findAllByCityIds(citiesIds));
            }
            case NULL_BESIDES_FROM -> ResponseEntity.status(HttpStatus.OK).body(
                    temperatureRepository.findAllByTimestampAfter(from));
            case NULL_BESIDES_UNTIL ->
                    ResponseEntity.status(HttpStatus.OK).body(temperatureRepository.findAllByTimestampBefore(until));
            case NULL_BESIDES_COORDS -> {
                citiesIds = cityRepository.findIdsByLonAndLat(params.getLongitude(), params.getLatitude());
                yield ResponseEntity.status(HttpStatus.OK).body(temperatureRepository.findAllByCityIds(citiesIds));
            }
            case NULL_BESIDES_DATES -> ResponseEntity.status(HttpStatus.OK).body(temperatureRepository.
                    findAllByTimestampBetween(from, until));
            case NULL_BESIDES_LATITUDE_AND_FROM -> {
                citiesIds = cityRepository.findIdsByLat(params.getLatitude());
                yield ResponseEntity.status(HttpStatus.OK).body(temperatureRepository.
                        findAllByCitiesIdsAndTimestampAfter(from, citiesIds));
            }
            case NULL_BESIDES_LONGITUDE_AND_FROM -> {
                citiesIds = cityRepository.findIdsByLon(params.getLongitude());
                yield ResponseEntity.status(HttpStatus.OK).body(temperatureRepository.
                        findAllByCitiesIdsAndTimestampAfter(from, citiesIds));
            }
            case NULL_BESIDES_LATITUDE_AND_UNTIL -> {
                citiesIds = cityRepository.findIdsByLat(params.getLatitude());
                yield ResponseEntity.status(HttpStatus.OK).body(temperatureRepository.
                        findAllByCitiesIdsAndTimestampBefore(until, citiesIds));
            }
            case NULL_BESIDES_LONGITUDE_AND_UNTIL -> {
                citiesIds = cityRepository.findIdsByLon(params.getLongitude());
                yield ResponseEntity.status(HttpStatus.OK).body(temperatureRepository.
                        findAllByCitiesIdsAndTimestampBefore(until, citiesIds));
            }
            case LATITUDE_NULL -> {
                citiesIds = cityRepository.findIdsByLon(params.getLongitude());
                yield ResponseEntity.status(HttpStatus.OK).body(temperatureRepository.
                        findAllByCitiesIdsAndTimestampBetween(from, until, citiesIds));
            }
            case LONGITUDE_NULL -> {
                citiesIds = cityRepository.findIdsByLat(params.getLatitude());
                yield ResponseEntity.status(HttpStatus.OK).body(temperatureRepository.
                        findAllByCitiesIdsAndTimestampBetween(from, until, citiesIds));
            }
            case FROM_NULL -> {
                citiesIds = cityRepository.findIdsByLonAndLat(params.getLongitude(), params.getLatitude());
                yield ResponseEntity.status(HttpStatus.OK).body(temperatureRepository.
                        findAllByCitiesIdsAndTimestampBefore(until, citiesIds));
            }
            case UNTIL_NULL -> {
                citiesIds = cityRepository.findIdsByLonAndLat(params.getLongitude(), params.getLatitude());
                yield ResponseEntity.status(HttpStatus.OK).body(temperatureRepository.
                        findAllByCitiesIdsAndTimestampAfter(from, citiesIds));
            }
            case DEFAULT -> {
                citiesIds = cityRepository.findIdsByLonAndLat(params.getLongitude(), params.getLatitude());
                yield ResponseEntity.status(HttpStatus.OK).body(temperatureRepository.
                        findAllByCitiesIdsAndTimestampBetween(from, until, citiesIds));
            }
        };
    }

    public ResponseEntity<Object> getTemperaturesCityByDate(TemperatureParams params, Integer cityId) {
        List<Integer> citiesId = new ArrayList<>();

        citiesId.add(cityId);
        LocalDateTime from = null;
        LocalDateTime until = null;

        // Check if the city id provided in path exists
        if (!cityRepository.existsById(cityId)) {
            return ResponseEntity.status(HttpStatus.OK).body("[]");
        }

        // Set the timers so they correspond with the timestamp in database
        if (params.getFrom() != null) {
            from = LocalDateTime.parse(params.getFrom() + "T00:00:00");
        }

        if (params.getUntil() != null) {
            until = LocalDateTime.parse(params.getUntil() + "T23:59:59");
        }

        // Return the temperatures based on the parameters provided
        return switch (params.whichCase()) {
            case NULL_BESIDES_COORDS ->
                    ResponseEntity.status(HttpStatus.OK).body(temperatureRepository.findById(cityId));
            case NULL_BESIDES_FROM -> ResponseEntity.status(HttpStatus.OK).body(temperatureRepository.
                    findAllByCitiesIdsAndTimestampAfter(from, citiesId));
            case NULL_BESIDES_UNTIL -> ResponseEntity.status(HttpStatus.OK).body(temperatureRepository.
                    findAllByCitiesIdsAndTimestampBefore(until, citiesId));
            default -> ResponseEntity.status(HttpStatus.OK).body(temperatureRepository.
                    findAllByCityIds(citiesId));
        };
    }

    // Corner-Case if no id was provided in the path
    public ResponseEntity<Object> getTemperaturesCityByDate2() {
        return ResponseEntity.status(HttpStatus.OK).body("[]");
    }

    public ResponseEntity<Object> getAllTemperaturesByCountry(Integer idCountry) {
        // Check if the country id exists in the db
        if (!countryRepository.existsById(idCountry)) {
            return ResponseEntity.status(HttpStatus.OK).body("[]");
        }

        List<Integer> citiesId = cityRepository.findAllIdsByIdTara(idCountry);

        return ResponseEntity.status(HttpStatus.OK).body(temperatureRepository.
                findAllByCityIds(citiesId));
    }

    // Corner-Case if no id was provided in the path
    public ResponseEntity<Object> getAllTemperaturesByCountry2() {
        return ResponseEntity.status(HttpStatus.OK).body("[]");
    }

    @Transactional
    public ResponseEntity<Object> deleteTemperature(Integer idTemperature) {

        if (idTemperature == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (!temperatureRepository.existsById(idTemperature)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        temperatureRepository.deleteById(idTemperature);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


}

package com.weatherapp.crud_api.temperature;

import com.weatherapp.crud_api.cities.City;
import com.weatherapp.crud_api.cities.CityRepository;
import com.weatherapp.crud_api.countries.CountryRepository;
import jakarta.transaction.Transactional;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
        // check if id_oras exists
        if (temperature.getIdOras() == 0 || temperature.getValoare() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 400
        }

        if (cityRepository.existsById(temperature.getIdOras())) {
            if (temperatureRepository.existsByTimestamp(LocalDateTime.now())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409
            }
            Temperature temp = new Temperature();
            temp.setId_oras(temperature.getIdOras());
            temp.setTemperature(temperature.getValoare());
            temp.setTimestamp(LocalDateTime.now());
            temperatureRepository.save(temp);
            return ResponseEntity.status(HttpStatus.CREATED).body("{\"id\": " + temp.getId() + "}");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
        }
    }

    public ResponseEntity<Object> updateTemperature(TemperatureDTO temperature, Integer id) {
        if (id == null || temperature.getIdOras() == null || temperature.getValoare() == null
                || temperatureRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 400
        }

        if (cityRepository.existsById(temperature.getIdOras())) {
            Optional<Temperature> existingTemp = temperatureRepository.findById(id);
            existingTemp.get().setTemperature(temperature.getValoare());
            temperatureRepository.save(existingTemp.get());
            return ResponseEntity.status(HttpStatus.OK).build();
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    public ResponseEntity<Object> getTemperatureByConditions(TemperatureParams params) {
        if (params.isNull()) {
            return ResponseEntity.status(HttpStatus.OK).body("[]");
        }

        List<Integer> citiesIds;
        LocalDateTime from = null;
        LocalDateTime until = null;
        if (params.getFrom() != null) {
            from = LocalDateTime.parse(params.getFrom() + "T00:00:00");
        }
        if (params.getUntil() != null) {
            until = LocalDateTime.parse(params.getUntil() + "T23:59:59");
        }
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

        if (!cityRepository.existsById(cityId)) {
            return ResponseEntity.status(HttpStatus.OK).body("[]");
        }

        if (params.getFrom() != null) {
            from = LocalDateTime.parse(params.getFrom() + "T00:00:00");
        }

        if (params.getUntil() != null) {
            until = LocalDateTime.parse(params.getUntil() + "T23:59:59");
        }

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

    public ResponseEntity<Object> getTemperaturesCityByDate2() {
        return ResponseEntity.status(HttpStatus.OK).body("[]");
    }

    public ResponseEntity<Object> getAllTemperaturesByCountry(Integer idCountry) {
        if (!countryRepository.existsById(idCountry)) {
            return ResponseEntity.status(HttpStatus.OK).body("[]");
        }

        List<Integer> citiesId = cityRepository.findAllIdsByIdTara(idCountry);

        return ResponseEntity.status(HttpStatus.OK).body(temperatureRepository.
                findAllByCityIds(citiesId));
    }

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

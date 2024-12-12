package com.weatherapp.crud_api.cities;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CityRepository extends CrudRepository<City, Integer> {

    Optional<City> findByNume(String nume);
    Optional<City> findById(Integer id);
    Iterable<City> findAllByIdTara(int id);
    boolean existsById(Integer id);

    @Query("SELECT c.id FROM City c WHERE c.lat = :latitude")
    List<Integer> findIdsByLat(@Param("latitude") Double latitude);

    @Query("SELECT c.id FROM City c WHERE c.lon = :longitude")
    List<Integer> findIdsByLon(@Param("longitude") Double longitude);

    @Query("SELECT c.id FROM City c WHERE c.lon = :longitude AND c.lat = :latitude")
    List<Integer> findIdsByLonAndLat(@Param("longitude") Double longitude, @Param("latitude") Double latitude);

    @Query("SELECT c.id FROM City c WHERE c.idTara = :idCountry")
    List<Integer> findAllIdsByIdTara(@Param("idCountry") Integer idCountry);



}

package com.weatherapp.crud_api.countries;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CountryRepository extends CrudRepository<Country, String> {
    Optional<Country> findByNume(String nume);
    Optional<Country> findById(Integer id);
    Optional<Country> deleteById(Integer id);
    boolean existsById(Integer id);
}

package com.weatherapp.crud_api.temperature;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface TemperatureRepository extends CrudRepository<Temperature, Integer> {
    boolean existsByTimestamp(LocalDateTime timestamp);

    @Query("SELECT t.id AS id, t.temperature AS valoare, " +
            "TO_CHAR(t.timestamp, 'YYYY-MM-DD') AS timestamp " +
            "FROM Temperature t")

    Iterable<Map<String, Object>> findAllPretty();

    @Query("SELECT t.id AS id, t.temperature AS valoare, " +
            "TO_CHAR(t.timestamp, 'YYYY-MM-DD') AS timestamp " +
            "FROM Temperature t WHERE t.idOras IN :cityIds")
    List<Map<String, Object>> findAllByCityIds(@Param("cityIds") List<Integer> cityIds);

    @Query("SELECT t.id AS id, t.temperature AS valoare, " +
            "TO_CHAR(t.timestamp, 'YYYY-MM-DD') AS timestamp " +
            "FROM Temperature t WHERE t.timestamp >= :time")
    List<Map<String, Object>> findAllByTimestampAfter(@Param("time") LocalDateTime time);

    @Query("SELECT t.id AS id, t.temperature AS valoare, " +
            "TO_CHAR(t.timestamp, 'YYYY-MM-DD') AS timestamp " +
            "FROM Temperature t WHERE t.timestamp <= :time")
    List<Map<String, Object>> findAllByTimestampBefore(@Param("time") LocalDateTime time);

    @Query("SELECT t.id AS id, t.temperature AS valoare, " +
            "TO_CHAR(t.timestamp, 'YYYY-MM-DD') AS timestamp " +
            "FROM Temperature t WHERE t.timestamp BETWEEN :from AND :until")
    List<Map<String, Object>> findAllByTimestampBetween(@Param("from") LocalDateTime from,
                                                       @Param("until") LocalDateTime until);

    @Query("SELECT t.id AS id, t.temperature AS valoare, " +
            "TO_CHAR(t.timestamp, 'YYYY-MM-DD') AS timestamp " +
            "FROM Temperature t WHERE t.timestamp >= :time AND t.id in :citiesIds")
    List<Map<String, Object>> findAllByCitiesIdsAndTimestampAfter(@Param("time") LocalDateTime time,
                                                                  @Param("citiesIds") List<Integer> citiesIds);

    @Query("SELECT t.id AS id, t.temperature AS valoare, " +
            "TO_CHAR(t.timestamp, 'YYYY-MM-DD') AS timestamp " +
            "FROM Temperature t WHERE t.timestamp <= :time AND t.id in :citiesIds")
    List<Map<String, Object>> findAllByCitiesIdsAndTimestampBefore(@Param("time") LocalDateTime time,
                                                                  @Param("citiesIds") List<Integer> citiesIds);

    @Query("SELECT t.id AS id, t.temperature AS valoare, " +
            "TO_CHAR(t.timestamp, 'YYYY-MM-DD') AS timestamp " +
            "FROM Temperature t WHERE (t.timestamp BETWEEN :from AND :until) AND t.id in :citiesIds")
    List<Map<String, Object>> findAllByCitiesIdsAndTimestampBetween(@Param("from") LocalDateTime from,
                                                                   @Param("until") LocalDateTime until,
                                                                   @Param("citiesIds") List<Integer> citiesIds);

}

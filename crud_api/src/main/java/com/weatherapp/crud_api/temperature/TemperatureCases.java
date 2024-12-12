package com.weatherapp.crud_api.temperature;

public enum TemperatureCases {
    NULL_BESIDES_LATITUDE(1),
    NULL_BESIDES_LONGITUDE(2),
    NULL_BESIDES_FROM(3),
    NULL_BESIDES_UNTIL(4),
    NULL_BESIDES_COORDS(5),
    NULL_BESIDES_DATES(6),
    NULL_BESIDES_LATITUDE_AND_FROM(7),
    NULL_BESIDES_LONGITUDE_AND_FROM(8),
    NULL_BESIDES_LATITUDE_AND_UNTIL(9),
    NULL_BESIDES_LONGITUDE_AND_UNTIL(10),
    LATITUDE_NULL(11),
    LONGITUDE_NULL(12),
    FROM_NULL(13),
    UNTIL_NULL(14),
    DEFAULT(0);

    private final int value;

    TemperatureCases(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}

package com.weatherapp.crud_api.temperature;

public class TemperatureParams {
    private final Double latitude;
    private final Double longitude;

    private String from;
    private final String until;

    public TemperatureParams(Double latitude, Double longitude, String from, String until) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.from = from;
        this.until = until;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getFrom() {
        return from;
    }



    public String getUntil() {
        return until;
    }

    public boolean isNull() {
        return latitude == null && longitude == null && from == null && until == null;
    }

    public boolean isNullBesidesLatitude() {
        return longitude == null && from == null && until == null;
    }

    public boolean isNullBesidesLongitude() {
        return latitude == null && from == null && until == null;
    }

    public boolean isNullBesidesFrom() {
        return latitude == null && longitude == null && until == null;
    }

    public boolean isNullBesidesUntil() {
        return latitude == null && longitude == null && from == null;
    }

    public boolean isNullBesidesCoords() {
        return from == null && until == null;
    }

    public boolean isNullBesidesDates() {
        return latitude == null && longitude == null;
    }

    public boolean isNullBesidesLatitudeAndFrom() {
        return longitude == null && until == null;
    }

    public boolean isNullBesidesLongitudeAndFrom() {
        return latitude == null && until == null;
    }

    public boolean isNullBesidesLatitudeAndUntil() {
        return longitude == null && from == null;
    }

    public boolean isNullBesidesLongitudeAndUntil() {
        return latitude == null && from == null;
    }

    public boolean isLatitudeNull() {
        return latitude == null;
    }

    public boolean isLongitudeNull() {
        return longitude == null;
    }

    public boolean isFromNull() {
        return from == null;
    }

    public boolean isUntilNull() {
        return until == null;
    }

    public TemperatureCases whichCase() {
        if (isNullBesidesLatitude()) {
            return TemperatureCases.NULL_BESIDES_LATITUDE;
        } else if (isNullBesidesLongitude()) {
            return TemperatureCases.NULL_BESIDES_LONGITUDE;
        } else if (isNullBesidesFrom()) {
            return TemperatureCases.NULL_BESIDES_FROM;
        } else if (isNullBesidesUntil()) {
            return TemperatureCases.NULL_BESIDES_UNTIL;
        } else if (isNullBesidesCoords()) {
            return TemperatureCases.NULL_BESIDES_COORDS;
        } else if (isNullBesidesDates()) {
            return TemperatureCases.NULL_BESIDES_DATES;
        } else if (isNullBesidesLatitudeAndFrom()) {
            return TemperatureCases.NULL_BESIDES_LATITUDE_AND_FROM;
        } else if (isNullBesidesLongitudeAndFrom()) {
            return TemperatureCases.NULL_BESIDES_LONGITUDE_AND_FROM;
        } else if (isNullBesidesLatitudeAndUntil()) {
            return TemperatureCases.NULL_BESIDES_LATITUDE_AND_UNTIL;
        } else if (isNullBesidesLongitudeAndUntil()) {
            return TemperatureCases.NULL_BESIDES_LONGITUDE_AND_UNTIL;
        } else if (isLatitudeNull()) {
            return TemperatureCases.LATITUDE_NULL;
        } else if (isLongitudeNull()) {
            return TemperatureCases.LONGITUDE_NULL;
        } else if (isFromNull()) {
            return TemperatureCases.FROM_NULL;
        } else if (isUntilNull()) {
            return TemperatureCases.UNTIL_NULL;
        }
        return TemperatureCases.DEFAULT;
    }


}

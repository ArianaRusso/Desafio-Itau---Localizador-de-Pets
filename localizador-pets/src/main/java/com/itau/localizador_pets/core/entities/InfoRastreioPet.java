package com.itau.localizador_pets.core.entities;

import java.time.LocalDateTime;

public class InfoRastreioPet {

    private final String idSensor;
    private final double latitude;
    private final double longitude;
    private final LocalDateTime dataHora;

    public InfoRastreioPet(String idSensor, double latitude, double longitude, LocalDateTime dataHora) {
        this.idSensor = idSensor;
        this.latitude = latitude;
        this.longitude = longitude;
        this.dataHora = dataHora;
    }

    public String getIdSensor() {
        return idSensor;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public String getCoordenadas() {
        return latitude + "," + longitude;
    }
}

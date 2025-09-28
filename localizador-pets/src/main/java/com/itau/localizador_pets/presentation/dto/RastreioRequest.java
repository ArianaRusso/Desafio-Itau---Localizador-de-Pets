package com.itau.localizador_pets.presentation.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record RastreioRequest(
        @Schema(example = "COL-12345", description = "Identificador único da coleira")
        @NotBlank(message = "não pode ser nulo ou vazio")
        String idSensor,

        @Schema(example = "-23.561684", description = "Latitude do último ponto de rastreio")
        @NotNull(message = "não pode ser nulo")
        Double latitude,


        @Schema(example = "-46.655981", description = "Longitude do último ponto de rastreio")
        @NotNull(message = "não pode ser nulo")
        Double longitude,

        @Schema(example = "2025-09-27T15:30:00", description = "Data e hora da última localização")
        @NotNull(message = "não pode ser nulo")
        LocalDateTime dataHora
) {
}

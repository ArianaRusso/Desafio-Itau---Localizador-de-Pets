package com.itau.localizador_pets.presentation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public record ErrorResponse(LocalDateTime timestamp,
                            int status,
                            String error,
                            String path) {
}

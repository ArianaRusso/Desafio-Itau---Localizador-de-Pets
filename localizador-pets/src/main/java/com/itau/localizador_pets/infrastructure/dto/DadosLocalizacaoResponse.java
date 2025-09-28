package com.itau.localizador_pets.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public record DadosLocalizacaoResponse(
        String label,
        String name,
        String street,
        String number,
        String postal_code,
        String region,
        String region_code,
        String county,
        String locality,
        String country,
        String country_code,
        String neighbourhood
) {
}
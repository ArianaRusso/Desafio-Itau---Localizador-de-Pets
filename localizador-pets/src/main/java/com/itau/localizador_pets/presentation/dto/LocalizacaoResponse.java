package com.itau.localizador_pets.presentation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public record LocalizacaoResponse(
        String pais,
        String estado,
        String cidade,
        String bairro,
        String endereco
) {
}

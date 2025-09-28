package com.itau.localizador_pets.infrastructure.mapper;

import com.itau.localizador_pets.core.entities.LocalizacaoPet;
import com.itau.localizador_pets.infrastructure.dto.DadosLocalizacaoResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClientToDomainMapper {
    public LocalizacaoPet clientToDomain(DadosLocalizacaoResponse dados) {
        return new LocalizacaoPet(
                dados.country(),
                dados.region_code(),
                dados.region(),
                dados.neighbourhood(),
                dados.street()
        );
    }

    public List<LocalizacaoPet> clientToDomain(List<DadosLocalizacaoResponse> dados) {
        return dados.stream().map(this::clientToDomain).toList();
    }
}

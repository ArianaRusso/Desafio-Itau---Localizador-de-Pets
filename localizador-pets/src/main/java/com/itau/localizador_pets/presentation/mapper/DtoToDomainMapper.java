package com.itau.localizador_pets.presentation.mapper;

import com.itau.localizador_pets.core.entities.InfoRastreioPet;
import com.itau.localizador_pets.core.entities.LocalizacaoPet;
import com.itau.localizador_pets.presentation.dto.LocalizacaoResponse;
import com.itau.localizador_pets.presentation.dto.RastreioRequest;
import org.springframework.stereotype.Component;

@Component
public class DtoToDomainMapper {
    public InfoRastreioPet toDomain(RastreioRequest dto) {
        return new InfoRastreioPet(
                dto.idSensor(),
                dto.latitude(),
                dto.longitude(),
                dto.dataHora());
    }

    public LocalizacaoResponse toDto(LocalizacaoPet entity) {
        return new LocalizacaoResponse(
                entity.getPais(),
                entity.getEstado(),
                entity.getCidade(),
                entity.getBairro(),
                entity.getEndereco()
        );
    }

}

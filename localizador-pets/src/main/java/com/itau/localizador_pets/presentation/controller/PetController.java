package com.itau.localizador_pets.presentation.controller;

import com.itau.localizador_pets.core.entities.LocalizacaoPet;
import com.itau.localizador_pets.core.service.LocalizadorService;
import com.itau.localizador_pets.infrastructure.mapper.ClientToDomainMapper;
import com.itau.localizador_pets.presentation.dto.LocalizacaoResponse;
import com.itau.localizador_pets.presentation.dto.RastreioRequest;
import com.itau.localizador_pets.presentation.mapper.DtoToDomainMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/pet")
@AllArgsConstructor
public class PetController {

    private final LocalizadorService service;
    private final DtoToDomainMapper mapper;

    @PostMapping("/localizacao")
    public ResponseEntity<LocalizacaoResponse> obterLocalizacao(@Valid @RequestBody RastreioRequest request){
        LocalizacaoPet localizacaoPet = service.obterLocalizacao(mapper.toDomain(request));
        return ResponseEntity.status(HttpStatus.OK).body(mapper.toDto(localizacaoPet));
    }
}

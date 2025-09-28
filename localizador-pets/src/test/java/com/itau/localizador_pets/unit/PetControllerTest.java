package com.itau.localizador_pets.unit;

import com.itau.localizador_pets.core.entities.InfoRastreioPet;
import com.itau.localizador_pets.core.entities.LocalizacaoPet;
import com.itau.localizador_pets.core.exceptions.LocalizacaoNaoEncontradaException;
import com.itau.localizador_pets.core.service.LocalizadorService;
import com.itau.localizador_pets.presentation.controller.PetController;
import com.itau.localizador_pets.presentation.dto.LocalizacaoResponse;
import com.itau.localizador_pets.presentation.dto.RastreioRequest;
import com.itau.localizador_pets.presentation.mapper.DtoToDomainMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PetControllerTest {
    @Mock
    LocalizadorService service;

    @Mock
    DtoToDomainMapper mapper;

    @InjectMocks
    PetController controller;

    RastreioRequest request;

    InfoRastreioPet infoRastreioPet;

    LocalizacaoPet localizacaoPet;

    @BeforeEach
    void setUp() {
        request = new RastreioRequest("COL-12345", 123.0,456.0, LocalDateTime.now());
        infoRastreioPet = new InfoRastreioPet("COL-12345", 123.0,456.0, LocalDateTime.now());
        localizacaoPet = new LocalizacaoPet("pais", "estado", "cidade", "bairro", "rua");
    }

    @Test
    @DisplayName("Deve retornar ResponseEntity OK com LocalizacaoResponse quando a requisição é válida")
    void deveRetornarResponseEntityComLocalizacaoResponseQuandoRequisicaoValida() {

        LocalizacaoResponse response = new LocalizacaoResponse("pais", "estado", "cidade", "bairro", "rua");

        when(mapper.toDomain(request)).thenReturn(infoRastreioPet);
        when(service.obterLocalizacao(infoRastreioPet)).thenReturn(localizacaoPet);
        when(mapper.toDto(localizacaoPet)).thenReturn(response);

        ResponseEntity<LocalizacaoResponse> resultado = controller.obterLocalizacao(request);

        assertThat(resultado.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resultado.getBody()).isEqualTo(response);
        verify(mapper, times(1)).toDomain(request);
        verify(service, times(1)).obterLocalizacao(infoRastreioPet);
        verify(mapper, times(1)).toDto(localizacaoPet);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o mapper falhar ao converter request para domain")
    void deveLancarExcecaoQuandoMapperLancaExcecaoAoConverterRequestParaDomain() {
        when(mapper.toDomain(request)).thenThrow(new RuntimeException("Erro ao mapear request"));

        assertThrows(RuntimeException.class, () -> controller.obterLocalizacao(request));
        verify(mapper, times(1)).toDomain(request);
        verifyNoInteractions(service);
    }

    @Test
    @DisplayName("Deve lançar LocalizacaoNaoEncontradaException quando o service não encontrar a localização")
    void deveLancarExcecaoQuandoServicoLancaLocalizacaoNaoEncontradaException() {

        when(mapper.toDomain(request)).thenReturn(infoRastreioPet);
        when(service.obterLocalizacao(infoRastreioPet)).thenThrow(new LocalizacaoNaoEncontradaException("Localização não encontrada"));

        assertThrows(LocalizacaoNaoEncontradaException.class, () -> controller.obterLocalizacao(request));
        verify(mapper, times(1)).toDomain(request);
        verify(service, times(1)).obterLocalizacao(infoRastreioPet);
        verifyNoMoreInteractions(mapper);
    }
}
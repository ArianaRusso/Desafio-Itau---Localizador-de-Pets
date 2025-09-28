package com.itau.localizador_pets.unit;

import com.itau.localizador_pets.core.entities.InfoRastreioPet;
import com.itau.localizador_pets.core.entities.LocalizacaoPet;
import com.itau.localizador_pets.core.exceptions.ErroIntegracaoException;
import com.itau.localizador_pets.core.exceptions.LocalizacaoNaoEncontradaException;
import com.itau.localizador_pets.core.service.BuscarLocalizacaoClient;
import com.itau.localizador_pets.core.service.LocalizadorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LocalizadorServiceImplTest {

    @Mock
    BuscarLocalizacaoClient client;

    @InjectMocks
    LocalizadorServiceImpl service;

    InfoRastreioPet infoRastreioPet;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();
        infoRastreioPet = new InfoRastreioPet("COL-123", 123.0, 456, now);
    }

    @Test
    @DisplayName("Deve retornar LocalizacaoPet quando a localização é encontrada")
    void deveRetornarLocalizacaoPetQuandoLocalizacaoEncontrada() {
        LocalizacaoPet localizacaoPet = new LocalizacaoPet("Brasil", "SP", "São Paulo", "Centro", "Rua A, 123");

        when(client.buscarLocalizacao("123.0,456.0")).thenReturn(List.of(localizacaoPet));

        LocalizacaoPet resultado = service.obterLocalizacao(infoRastreioPet);

        assertEquals(localizacaoPet, resultado);
        verify(client, times(1)).buscarLocalizacao("123.0,456.0");
    }

    @Test
    @DisplayName("Deve lançar LocalizacaoNaoEncontradaException quando a localização não é encontrada")
    void deveLancarLocalizacaoNaoEncontradaExceptionQuandoLocalizacaoNaoEncontrada() {
        when(client.buscarLocalizacao("123.0,456.0")).thenReturn(new ArrayList<>());

        LocalizacaoNaoEncontradaException exception = assertThrows(LocalizacaoNaoEncontradaException.class,
                () -> service.obterLocalizacao(infoRastreioPet));

        assertEquals("Localização não encontrada para as coordenadas: 123.0,456.0", exception.getMessage());
        verify(client, times(1)).buscarLocalizacao("123.0,456.0");
    }

    @Test
    @DisplayName("Deve lançar ErroIntegracaoException quando ocorre um erro inesperado")
    void deveLancarErroIntegracaoExceptionQuandoErroInesperado() {
        when(client.buscarLocalizacao("123.0,456.0")).thenThrow(new RuntimeException("Erro genérico"));

        ErroIntegracaoException exception = assertThrows(ErroIntegracaoException.class, () -> service.obterLocalizacao(infoRastreioPet));

        assertEquals("Erro ao integrar com PositionStack", exception.getMessage());
        verify(client, times(1)).buscarLocalizacao("123.0,456.0");
    }
}
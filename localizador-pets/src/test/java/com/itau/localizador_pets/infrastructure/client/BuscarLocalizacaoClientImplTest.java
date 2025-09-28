package com.itau.localizador_pets.infrastructure.client;

import com.itau.localizador_pets.core.entities.LocalizacaoPet;
import com.itau.localizador_pets.infrastructure.dto.DadosLocalizacaoResponse;
import com.itau.localizador_pets.infrastructure.dto.LocalizaoClientResponse;
import com.itau.localizador_pets.infrastructure.mapper.ClientToDomainMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscarLocalizacaoClientImplTest {

    @InjectMocks
    BuscarLocalizacaoClientImpl client;

    @Mock
    RastreioLocalizacaoClient rastreioClient;

    @Mock
    ClientToDomainMapper mapper;

    private final String chaveApi = System.getenv("POSITIONSTACK_API_KEY");

    private LocalizaoClientResponse localizaoClientResponse;
    private String coordenadas;

    @BeforeEach
    void setUp() {
        List<DadosLocalizacaoResponse> dadosLocalizacaoResponses = new ArrayList<>();
        dadosLocalizacaoResponses.add(new DadosLocalizacaoResponse("label", "name", "street", "number",
                "postal_code", "region", "region_code", "county", "locality", "country", "country_code",
                "neighborhood"));
        localizaoClientResponse = new LocalizaoClientResponse(dadosLocalizacaoResponses);
        coordenadas = "123.0,456.0";
    }


    @Test
    @DisplayName("Deve retornar lista de LocalizacaoPet quando o mapeamento é bem-sucedido")
    void deveRetornarListaDeLocalizacaoPetQuandoMapeamentoBemSucedido() {
        List<LocalizacaoPet> localizacoes = List.of(new LocalizacaoPet("county", "region_code", "region", "neighborhood", "street"));

        when(rastreioClient.buscarLocalizacao(chaveApi, coordenadas)).thenReturn(localizaoClientResponse);
        when(mapper.clientToDomain(localizaoClientResponse.data())).thenReturn(localizacoes);

        List<LocalizacaoPet> resultado = client.buscarLocalizacao(coordenadas);

        assertThat(resultado)
                .isNotEmpty()
                .containsExactlyElementsOf(localizacoes);

        verify(rastreioClient, times(1)).buscarLocalizacao(chaveApi,coordenadas);
        verify(mapper, times(1)).clientToDomain(localizaoClientResponse.data());
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando o mapeamento retorna lista vazia")
    void deveRetornarListaVaziaQuandoMapeamentoRetornaListaVazia() {

        when(rastreioClient.buscarLocalizacao(chaveApi, coordenadas)).thenReturn(localizaoClientResponse);
        when(mapper.clientToDomain(localizaoClientResponse.data())).thenReturn(Collections.emptyList());

        List<LocalizacaoPet> resultado = client.buscarLocalizacao(coordenadas);

        assertTrue(resultado.isEmpty());

        verify(rastreioClient, times(1)).buscarLocalizacao(chaveApi,coordenadas);
        verify(mapper, times(1)).clientToDomain(localizaoClientResponse.data());
    }

    @Test
    @DisplayName("Deve lançar exceção quando client.buscarLocalizacao lança exceção")
    void deveLancarExcecaoQuandoClientBuscarLocalizacaoLancaExcecao() {
        String coordenadas = "123,456";

        when(rastreioClient.buscarLocalizacao(chaveApi, coordenadas)).thenThrow(new RuntimeException("Erro ao buscar localização"));

        assertThrows(RuntimeException.class, () -> client.buscarLocalizacao(coordenadas));

        verify(rastreioClient, times(1)).buscarLocalizacao(chaveApi, coordenadas);
        verifyNoInteractions(mapper);
    }



}
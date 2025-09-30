package com.itau.localizador_pets.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.itau.localizador_pets.presentation.dto.LocalizacaoResponse;
import com.itau.localizador_pets.presentation.dto.RastreioRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PetControllerIT {

    static WireMockServer wireMockServer;

    @LocalServerPort
    int port;

    RestTemplate restTemplate = new RestTemplate();

    @BeforeAll
    static void startWireMock() {
        wireMockServer = new WireMockServer(WireMockConfiguration.options().dynamicPort());
        wireMockServer.start();
        configureFor("localhost", wireMockServer.port());
    }

    @AfterAll
    static void stopWireMock() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("positionstack.api.url", () -> "http://localhost:" + wireMockServer.port());
    }

    @BeforeEach
    void setupWiremock() {
        wireMockServer.resetAll();
    }

    @Test
    @DisplayName("Deve retornar OK quando localização encontrada")
    void deveRetornar200QuandoLocalizacaoEncontrada() throws Exception {
        RastreioRequest request = new RastreioRequest("COL-12345", 123.0, 456.0,
               LocalDateTime.now());

        stubFor(get(urlPathEqualTo("/reverse"))
                .withQueryParam("query", equalTo("123.0,456.0"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                            {
                              "data": [
                                {
                                  "country": "Brasil",
                                  "region_code": "SP",
                                  "region": "São Paulo",
                                  "neighbourhood": "Centro",
                                  "street": "Rua A, 123"
                                }
                              ]
                            }
                            """)));

        String url = "http://localhost:" + port + "/v1/pet/localizacao";

        ResponseEntity<LocalizacaoResponse> responseEntity =
                restTemplate.postForEntity(url, request, LocalizacaoResponse.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().pais()).isEqualTo("Brasil");
        assertThat(responseEntity.getBody().estado()).isEqualTo("SP");
        assertThat(responseEntity.getBody().cidade()).isEqualTo("São Paulo");
        assertThat(responseEntity.getBody().bairro()).isEqualTo("Centro");
        assertThat(responseEntity.getBody().endereco()).isEqualTo("Rua A, 123");
    }

    @Test
    @DisplayName("Deve retornar NOT_FOUND quando não encontrar localização")
    void deveRetornar404QuandoLocalizacaoNaoEncontrada() throws Exception {
        RastreioRequest request = new RastreioRequest("COL-12345", 123.0, 456.0, LocalDateTime.now());

        wireMockServer.resetAll();

        stubFor(get(urlPathEqualTo("/reverse"))
                .withQueryParam("query", equalTo("123.0,456.0"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                        { "data": [] }
                    """)));

        String url = "http://localhost:" + port + "/v1/pet/localizacao";

        var exception = assertThrows(
                HttpClientErrorException.NotFound.class,
                () -> restTemplate.postForEntity(url, request, String.class)
        );

        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(exception.getResponseBodyAsString()).contains("Localização não encontrada para as coordenadas");
    }

    @Test
    @DisplayName("Deve retornar BAD_GATEWAY quando ocorrer erro na integração")
    void deveRetornar502QuandoErroNaIntegracao() throws Exception {
        RastreioRequest request = new RastreioRequest("COL-12345", 123.0, 456.0, LocalDateTime.now());

        wireMockServer.resetAll();
        stubFor(get(urlPathEqualTo("/reverse"))
                .withQueryParam("query", equalTo("123.0,456.0"))
                .willReturn(aResponse()
                        .withStatus(500)));

        String url = "http://localhost:" + port + "/v1/pet/localizacao";

        var exception = assertThrows(
                HttpServerErrorException.BadGateway.class,
                () -> restTemplate.postForEntity(url, request, String.class)
        );

        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_GATEWAY);
        assertThat(exception.getResponseBodyAsString()).contains("Erro ao integrar com PositionStack");
    }
}

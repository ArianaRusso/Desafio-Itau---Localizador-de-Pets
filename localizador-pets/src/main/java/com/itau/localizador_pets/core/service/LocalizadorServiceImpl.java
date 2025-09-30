package com.itau.localizador_pets.core.service;

import com.itau.localizador_pets.core.entities.InfoRastreioPet;
import com.itau.localizador_pets.core.entities.LocalizacaoPet;
import com.itau.localizador_pets.core.exceptions.ErroIntegracaoException;
import com.itau.localizador_pets.core.exceptions.LocalizacaoNaoEncontradaException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
public class LocalizadorServiceImpl implements LocalizadorService {

    private final BuscarLocalizacaoClient client;

    @Override
    public LocalizacaoPet obterLocalizacao(InfoRastreioPet infoRastreioPet) {
        log.info("Iniciando busca de localização para sensor {}", infoRastreioPet.getIdSensor());
        try {
            String coordenadas = infoRastreioPet.getCoordenadas();

            List<LocalizacaoPet> response = client.buscarLocalizacao(coordenadas);

            return Optional.ofNullable(response)
                    .stream()
                    .flatMap(List::stream)
                    .findFirst()
                    .orElseThrow(() ->
                            new LocalizacaoNaoEncontradaException("Localização não encontrada para as coordenadas: " + coordenadas));

        } catch (LocalizacaoNaoEncontradaException e) {
            log.warn("Localização não encontrada para sensor {}", infoRastreioPet.getIdSensor());

            throw e;
        } catch (Exception e) {
            log.error("Erro ao integrar com PositionStack", e);

            throw new ErroIntegracaoException("Erro ao integrar com PositionStack", e);
        }
    }
}

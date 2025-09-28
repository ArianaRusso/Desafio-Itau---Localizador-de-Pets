package com.itau.localizador_pets.core.service;

import com.itau.localizador_pets.core.entities.InfoRastreioPet;
import com.itau.localizador_pets.core.entities.LocalizacaoPet;
import com.itau.localizador_pets.core.exceptions.ErroIntegracaoException;
import com.itau.localizador_pets.core.exceptions.LocalizacaoNaoEncontradaException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class LocalizadorServiceImpl implements LocalizadorService {

    private final BuscarLocalizacaoClient client;

    @Override
    public LocalizacaoPet obterLocalizacao(InfoRastreioPet infoRastreioPet) {
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
            throw e;
        } catch (Exception e) {
            throw new ErroIntegracaoException("Erro ao integrar com PositionStack", e);
        }
    }
}

package com.itau.localizador_pets.infrastructure.client;

import com.itau.localizador_pets.core.entities.LocalizacaoPet;
import com.itau.localizador_pets.core.service.BuscarLocalizacaoClient;
import com.itau.localizador_pets.infrastructure.mapper.ClientToDomainMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
@Slf4j
public class BuscarLocalizacaoClientImpl implements BuscarLocalizacaoClient {

    private final RastreioLocalizacaoClient client;
    private final String chaveApi = System.getenv("POSITIONSTACK_API_KEY");
    private final ClientToDomainMapper mapper;

    @Override
    public List<LocalizacaoPet> buscarLocalizacao(String coordenadas) {
        List<LocalizacaoPet> localizacaoPetList = mapper.clientToDomain(client.buscarLocalizacao(chaveApi, coordenadas).data());

        log.debug("Localização encontrada para coordenadas: {}", coordenadas);

        return localizacaoPetList;
    }
}

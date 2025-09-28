package com.itau.localizador_pets.infrastructure.client;

import com.itau.localizador_pets.core.entities.LocalizacaoPet;
import com.itau.localizador_pets.infrastructure.mapper.ClientToDomainMapper;
import com.itau.localizador_pets.core.service.BuscarLocalizacaoClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;



import java.util.List;

@AllArgsConstructor
@Service
public class BuscarLocalizacaoClientImpl implements BuscarLocalizacaoClient {

    private final RastreioLocalizacaoClient client;
    private final String chaveApi = System.getenv("POSITIONSTACK_API_KEY");
    private final ClientToDomainMapper mapper;

    @Override
    public List<LocalizacaoPet> buscarLocalizacao(String coordenadas) {
        return mapper.clientToDomain(client.buscarLocalizacao(chaveApi, coordenadas).data());
    }
}

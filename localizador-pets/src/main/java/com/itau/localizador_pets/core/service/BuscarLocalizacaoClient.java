package com.itau.localizador_pets.core.service;

import com.itau.localizador_pets.core.entities.LocalizacaoPet;

import java.util.List;

public interface BuscarLocalizacaoClient {
    List<LocalizacaoPet> buscarLocalizacao(String coordenadas);
}

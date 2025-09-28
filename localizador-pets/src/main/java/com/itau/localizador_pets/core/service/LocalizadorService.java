package com.itau.localizador_pets.core.service;

import com.itau.localizador_pets.core.entities.InfoRastreioPet;
import com.itau.localizador_pets.core.entities.LocalizacaoPet;

public interface LocalizadorService {
    LocalizacaoPet obterLocalizacao(InfoRastreioPet infoRastreioPet);
}

package com.itau.localizador_pets.core.entities;

import com.itau.localizador_pets.core.exceptions.LocalizacaoNaoEncontradaException;

public class LocalizacaoPet {

    private final String pais;
    private final String estado;
    private final String cidade;
    private final String bairro;
    private final String endereco;

    public LocalizacaoPet(String pais, String estado, String cidade, String bairro, String endereco) {
        if (pais == null || estado == null || cidade == null) {
            throw new LocalizacaoNaoEncontradaException("Localização não possui dados suficientes para identificação.");
        }
        this.pais = pais;
        this.estado = estado;
        this.cidade = cidade;
        this.bairro = bairro;
        this.endereco = endereco;
    }

    public String getPais() {
        return pais;
    }

    public String getEstado() {
        return estado;
    }

    public String getCidade() {
        return cidade;
    }

    public String getBairro() {
        return bairro;
    }

    public String getEndereco() {
        return endereco;
    }
}

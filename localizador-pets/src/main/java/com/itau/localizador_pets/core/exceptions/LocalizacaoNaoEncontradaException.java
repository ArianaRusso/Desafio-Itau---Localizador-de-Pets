package com.itau.localizador_pets.core.exceptions;

public class LocalizacaoNaoEncontradaException extends RuntimeException {
    public LocalizacaoNaoEncontradaException(String mensagem) {
        super(mensagem);
    }
}

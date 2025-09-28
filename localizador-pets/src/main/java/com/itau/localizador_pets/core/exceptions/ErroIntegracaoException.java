package com.itau.localizador_pets.core.exceptions;

public class ErroIntegracaoException extends RuntimeException {
    public ErroIntegracaoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
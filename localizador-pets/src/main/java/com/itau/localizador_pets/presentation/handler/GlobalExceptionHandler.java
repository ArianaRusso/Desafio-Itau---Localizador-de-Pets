package com.itau.localizador_pets.presentation.handler;

import com.itau.localizador_pets.core.exceptions.ErroIntegracaoException;
import com.itau.localizador_pets.core.exceptions.LocalizacaoNaoEncontradaException;
import com.itau.localizador_pets.presentation.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LocalizacaoNaoEncontradaException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(LocalizacaoNaoEncontradaException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(LocalDateTime.now(), 404, ex.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(ErroIntegracaoException.class)
    public ResponseEntity<ErrorResponse> handleIntegration(ErroIntegracaoException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(new ErrorResponse(LocalDateTime.now(), 502, ex.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String mensagemErro = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + " " + error.getDefaultMessage())
                .findFirst()
                .orElse("Requisição inválida");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        LocalDateTime.now(),
                        400,
                        mensagemErro,
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(LocalDateTime.now(), 500, "Erro inesperado", request.getRequestURI()));
    }
}

package br.com.caixa.desafio.exceptions;

import lombok.Getter;

@Getter
public class CaixaVersoBOException extends RuntimeException {
    private final String title;
    private final int status;
    private final String detail;

    public CaixaVersoBOException(String title, int status, String detail) {
        this.title = title;
        this.status = status;
        this.detail = detail;
    }
}

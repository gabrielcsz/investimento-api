package br.com.caixa.desafio.exceptions;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "ErrorResponse", description = "Detalhes do erro retornado pela API")
public record ErrorResponse(
        @Schema(description = "Título resumido do erro", example = "Erro de validação")
        String title,

        @Schema(description = "Código HTTP do erro", example = "400")
        int status,

        @Schema(description = "Mensagem detalhada do erro", example = "O campo 'valor' não pode ser nulo")
        String detail
) {}

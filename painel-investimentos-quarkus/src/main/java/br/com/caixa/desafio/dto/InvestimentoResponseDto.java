package br.com.caixa.desafio.dto;

import lombok.Builder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Schema(name = "InvestimentoResponseDto", description = "Dados do investimento do cliente")
public record InvestimentoResponseDto(

        @Schema(description = "ID do investimento", example = "10")
        Long id,

        @Schema(description = "Tipo do investimento (CDB, LCI, Fundos...)", example = "CDB")
        String tipo,

        @Schema(description = "Valor aplicado", example = "5000.00")
        BigDecimal valor,

        @Schema(description = "Rentabilidade contratada", example = "0.125")
        BigDecimal rentabilidade,

        @Schema(description = "Data da aplicação", example = "2025-10-20")
        LocalDate data
) {}

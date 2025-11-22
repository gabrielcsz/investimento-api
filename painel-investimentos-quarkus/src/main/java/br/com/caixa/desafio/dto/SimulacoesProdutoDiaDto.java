package br.com.caixa.desafio.dto;

import lombok.Builder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Schema(name = "SimulacoesProdutoDiaDto",
        description = "Resumo diário de simulações por produto.")
public record SimulacoesProdutoDiaDto(

        @Schema(description = "Nome do produto", example = "CDB Caixa 2026")
        String produto,

        @Schema(description = "Data da simulação agregada", example = "2025-11-21")
        LocalDate data,

        @Schema(description = "Quantidade total de simulações no dia", example = "42")
        Long quantidadeSimulacoes,

        @Schema(description = "Média do valor final das simulações", example = "1875.32")
        BigDecimal mediaValorFinal
) {}
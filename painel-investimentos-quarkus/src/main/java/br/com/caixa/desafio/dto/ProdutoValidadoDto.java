package br.com.caixa.desafio.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@Schema(description = "Informações do produto validado antes de realizar a simulação")
public class ProdutoValidadoDto {

    @Schema(
            description = "Identificador do produto",
            example = "101",
            required = true
    )
    private Long id;

    @Schema(
            description = "Nome comercial do produto de investimento",
            example = "CDB Caixa 2026",
            required = true
    )
    private String nome;

    @Schema(
            description = "Tipo do produto (CDB, LCI, LCA, FUNDOS, etc.)",
            example = "CDB",
            required = true
    )
    private String tipo;

    @Schema(
            description = "Rentabilidade anual bruta do produto",
            example = "0.12",
            required = true
    )
    private BigDecimal rentabilidade;

    @Schema(
            description = "Classificação de risco do produto",
            example = "Baixo",
            required = true
    )
    private String risco;
}

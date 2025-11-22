package br.com.caixa.desafio.dto;

import lombok.*;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(name = "ListaSimulacaoResponseDto", description = "Retorno com dados resumidos das simulações realizadas pelo cliente.")
public class ListaSimulacaoResponseDto {

    @Schema(description = "ID da simulação", example = "15")
    private Long id;

    @Schema(description = "ID do cliente", example = "202455")
    private Long clientId;

    @Schema(description = "Nome do produto utilizado na simulação", example = "CDB Caixa 2026")
    private String produto;

    @Schema(description = "Valor investido pelo cliente", example = "1500.00")
    private BigDecimal valorInvestido;

    @Schema(description = "Valor final calculado ao fim do prazo", example = "1890.45")
    private BigDecimal valorFinal;

    @Schema(description = "Prazo da simulação em meses", example = "12")
    private Integer prazoMeses;

    @Schema(description = "Data e hora em que a simulação foi realizada", example = "2025-11-21T14:25:10")
    private LocalDateTime dataSimulacao;
}

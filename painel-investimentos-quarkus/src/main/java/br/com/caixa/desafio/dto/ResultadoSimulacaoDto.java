package br.com.caixa.desafio.dto;

import lombok.*;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoSimulacaoDto {

    @Schema(
            description = "Valor final do investimento ao término do prazo",
            example = "11250.00",
            required = true
    )
    private BigDecimal valorFinal;
    @Schema(
            description = "Rentabilidade efetiva calculada para o período simulado (não anualizada)",
            example = "0.12",
            required = true
    )
    private BigDecimal rentabilidadeEfetiva;
    @Schema(
            description = "Prazo utilizado na simulação (em meses)",
            example = "12",
            required = true
    )
    private Integer prazoMeses;
}

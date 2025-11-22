package br.com.caixa.desafio.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDateTime;

@Builder
@Setter
@Getter
@Schema(description = "Resposta da simulação de investimento")
public class SimularResponseDto {

    @Schema(
            description = "Informações detalhadas sobre o produto validado para a simulação"
    )
    private ProdutoValidadoDto produtoValidado;
    @Schema(
            description = "Resultado final da simulação (rentabilidade, impostos, valor final, etc.)"
    )
    private ResultadoSimulacaoDto resultadoSimulacao;
    @Schema(
            description = "Data e hora em que a simulação foi processada",
            example = "2025-11-21T17:35:20"
    )
    private LocalDateTime dataSimulacao;
}

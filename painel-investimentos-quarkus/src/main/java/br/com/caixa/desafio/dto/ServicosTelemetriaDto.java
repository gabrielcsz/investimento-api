package br.com.caixa.desafio.dto;

import lombok.*;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Informações de telemetria de um serviço específico")
public class ServicosTelemetriaDto {

    @Schema(
            description = "Nome do serviço monitorado",
            example = "perfil-risco"
    )
    private String nome;

    @Schema(
            description = "Quantidade total de chamadas realizadas ao serviço",
            example = "142"
    )
    private Integer quantidadeChamadas;

    @Schema(
            description = "Tempo médio de resposta do serviço em milissegundos",
            example = "85.3"
    )
    private BigDecimal mediaTempoRespostaMs;
}

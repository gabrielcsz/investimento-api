package br.com.caixa.desafio.dto;

import lombok.*;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Período considerado na análise de telemetria")
public class PeriodoDto {

    @Schema(
            description = "Data inicial da análise de telemetria",
            example = "2025-01-01"
    )
    private LocalDate inicio;

    @Schema(
            description = "Data final da análise de telemetria",
            example = "2025-01-31"
    )
    private LocalDate fim;
}

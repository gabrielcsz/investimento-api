package br.com.caixa.desafio.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

@Schema(description = "Resposta contendo dados agregados de telemetria dos serviços")
public record TelemetriaResponseDto(

        @Schema(description = "Lista de serviços monitorados e suas estatísticas")
        List<ServicosTelemetriaDto> servicos,

        @Schema(description = "Período avaliado para a telemetria")
        PeriodoDto periodo
) {}

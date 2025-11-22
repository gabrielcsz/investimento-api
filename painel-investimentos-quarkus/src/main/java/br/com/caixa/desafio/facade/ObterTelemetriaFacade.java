package br.com.caixa.desafio.facade;

import br.com.caixa.desafio.dao.TelemetriaDao;
import br.com.caixa.desafio.dto.PeriodoDto;
import br.com.caixa.desafio.dto.ServicosTelemetriaDto;
import br.com.caixa.desafio.dto.TelemetriaResponseDto;
import br.com.caixa.desafio.entity.TelemetriaEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class ObterTelemetriaFacade {

    @Inject
    TelemetriaDao telemetriaDao;
    
    public Response executar() {
        List<TelemetriaEntity> registros = telemetriaDao.listarTelemetrias();

        List<ServicosTelemetriaDto> servicos = registros.stream()
                .map(telemetria -> {
                    String nomeServico = telemetria.getServico();
                    int quantidade = telemetria.getQuantidadeChamadas();
                    BigDecimal tempoMedioResposta = telemetria.getMediaTempoResposta();
                    return new ServicosTelemetriaDto(nomeServico,quantidade,tempoMedioResposta);
                }).toList();

        LocalDate inicio = registros.stream()
                .map(TelemetriaEntity::getPeriodoInicio)
                .min(LocalDate::compareTo)
                .orElse(LocalDate.now());

        LocalDate fim = registros.stream()
                .map(TelemetriaEntity::getPeriodoFim)
                .max(LocalDate::compareTo)
                .orElse(LocalDate.now());

        PeriodoDto periodo = new PeriodoDto(inicio, fim);
        var response = new TelemetriaResponseDto(servicos, periodo);

        return Response.ok(response).build();
    }
}

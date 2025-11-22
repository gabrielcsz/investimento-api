package br.com.caixa.desafio.filter;

import br.com.caixa.desafio.annotation.NomeServico;
import br.com.caixa.desafio.dao.TelemetriaDao;
import br.com.caixa.desafio.entity.TelemetriaEntity;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

@Provider
@Priority(Priorities.USER)
@ApplicationScoped
public class TelemetriaFilter implements ContainerRequestFilter, ContainerResponseFilter {

    @Context
    public ResourceInfo resourceInfo;
    private static final String START_TIME = "start-time";

    @Inject
    TelemetriaDao telemetriaDao;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        requestContext.setProperty(START_TIME, System.currentTimeMillis());
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        Object inicioObj = requestContext.getProperty(START_TIME);
        if (inicioObj == null) return;
        long inicio = (long) inicioObj;
        long fim = System.currentTimeMillis();
        long tempoResposta = fim - inicio;

        NomeServico nomeServico = resourceInfo.getResourceMethod().getAnnotation(NomeServico.class);
        if(nomeServico == null){
            nomeServico = resourceInfo.getResourceClass().getAnnotation(NomeServico.class);
        }
        String servico = nomeServico.value();
        if(servico.contains("/telemetria")) return;
        LocalDate dataAtual = LocalDate.now();

        telemetriaDao.procurarPorServico(servico).ifPresentOrElse(
                telemetria -> atualizarTelemetria(telemetria, tempoResposta, dataAtual),
                () -> criarNovaTelemetria(servico, tempoResposta, dataAtual)
        );
    }

    private void criarNovaTelemetria(String servico, long tempoResposta, LocalDate dataAtual) {
        TelemetriaEntity novaTelemetria = TelemetriaEntity.builder()
                .servico(servico)
                .mediaTempoResposta(BigDecimal.valueOf(tempoResposta))
                .quantidadeChamadas(1)
                .periodoInicio(dataAtual)
                .periodoFim(dataAtual)
                .build();

        telemetriaDao.persistirTelemetria(novaTelemetria);
    }

    private void atualizarTelemetria(TelemetriaEntity telemetria, long tempoResposta, LocalDate dataAtual) {
        int novasChamadas = telemetria.getQuantidadeChamadas() + 1;
        BigDecimal novaMedia = telemetria.getMediaTempoResposta()
                .multiply(BigDecimal.valueOf(telemetria.getQuantidadeChamadas()))
                .add(BigDecimal.valueOf(tempoResposta))
                .divide(BigDecimal.valueOf(novasChamadas), 2, BigDecimal.ROUND_HALF_UP);

        telemetria.setQuantidadeChamadas(novasChamadas);
        telemetria.setMediaTempoResposta(novaMedia);
        telemetria.setPeriodoInicio(dataAtual.isBefore(telemetria.getPeriodoInicio()) ? dataAtual : telemetria.getPeriodoInicio());
        telemetria.setPeriodoFim(dataAtual.isAfter(telemetria.getPeriodoFim()) ? dataAtual : telemetria.getPeriodoFim());

        telemetriaDao.persistirTelemetria(telemetria);
    }
}

package br.com.caixa.desafio.unitarios.filter;

import br.com.caixa.desafio.annotation.NomeServico;
import br.com.caixa.desafio.dao.TelemetriaDao;
import br.com.caixa.desafio.entity.TelemetriaEntity;
import br.com.caixa.desafio.filter.TelemetriaFilter;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ResourceInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TelemetriaFilterTest {

    @InjectMocks
    TelemetriaFilter filter;

    @Mock
    TelemetriaDao telemetriaDao;

    @Mock
    ResourceInfo resourceInfo;

    @Mock
    ContainerRequestContext requestContext;

    @Mock
    ContainerResponseContext responseContext;

    @BeforeEach
    void setup() {
        filter.resourceInfo = resourceInfo;
    }

    @Test
    void deveCriarNovaTelemetriaQuandoNaoExistir() throws Exception {
        when(requestContext.getProperty("start-time")).thenReturn(100L);

        Method metodo = FakeService.class.getMethod("acao");
        when(resourceInfo.getResourceMethod()).thenReturn(metodo);

        when(telemetriaDao.procurarPorServico("teste-servico"))
                .thenReturn(Optional.empty());

        filter.filter(requestContext, responseContext);

        ArgumentCaptor<TelemetriaEntity> captor = ArgumentCaptor.forClass(TelemetriaEntity.class);
        verify(telemetriaDao).persistirTelemetria(captor.capture());

        TelemetriaEntity salvo = captor.getValue();

        assert salvo.getServico().equals("teste-servico");
        assert salvo.getQuantidadeChamadas() == 1;
        assert salvo.getMediaTempoResposta() != BigDecimal.ZERO;
    }

    @Test
    void deveAtualizarTelemetriaExistente() throws Exception {
        when(requestContext.getProperty("start-time")).thenReturn(100L);

        Method metodo = FakeService.class.getMethod("acao");
        when(resourceInfo.getResourceMethod()).thenReturn(metodo);

        TelemetriaEntity existente = TelemetriaEntity.builder()
                .servico("teste-servico")
                .quantidadeChamadas(2)
                .mediaTempoResposta(BigDecimal.valueOf(100))
                .periodoInicio(LocalDate.now())
                .periodoFim(LocalDate.now())
                .build();

        when(telemetriaDao.procurarPorServico("teste-servico"))
                .thenReturn(Optional.of(existente));

        filter.filter(requestContext, responseContext);

        ArgumentCaptor<TelemetriaEntity> captor = ArgumentCaptor.forClass(TelemetriaEntity.class);
        verify(telemetriaDao).persistirTelemetria(captor.capture());

        TelemetriaEntity atualizado = captor.getValue();

        assert atualizado.getQuantidadeChamadas() == 3;
        assert atualizado.getMediaTempoResposta().compareTo(BigDecimal.ZERO) > 0;
    }


    static class FakeService {
        @NomeServico("teste-servico")
        public void acao() {}
    }
}

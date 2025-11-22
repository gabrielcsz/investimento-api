package br.com.caixa.desafio.unitarios.facade;

import br.com.caixa.desafio.dao.TelemetriaDao;
import br.com.caixa.desafio.dto.PeriodoDto;
import br.com.caixa.desafio.dto.ServicosTelemetriaDto;
import br.com.caixa.desafio.dto.TelemetriaResponseDto;
import br.com.caixa.desafio.entity.TelemetriaEntity;
import br.com.caixa.desafio.facade.ObterTelemetriaFacade;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class ObterTelemetriaFacadeTest {

    @Mock
    private TelemetriaDao telemetriaDao;

    @InjectMocks
    private ObterTelemetriaFacade facade;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveRetornarTelemetriaComSucesso() {

        TelemetriaEntity t1 = TelemetriaEntity.builder()
                .servico("Simulação")
                .quantidadeChamadas(10)
                .mediaTempoResposta(BigDecimal.valueOf(120))
                .periodoInicio(LocalDate.of(2024, 10, 1))
                .periodoFim(LocalDate.of(2024, 10, 10))
                .build();

        TelemetriaEntity t2 = TelemetriaEntity.builder()
                .servico("Investimento")
                .quantidadeChamadas(5)
                .mediaTempoResposta(BigDecimal.valueOf(80))
                .periodoInicio(LocalDate.of(2024, 9, 25))
                .periodoFim(LocalDate.of(2024, 10, 20))
                .build();

        when(telemetriaDao.listarTelemetrias()).thenReturn(List.of(t1, t2));

        Response response = facade.executar();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertNotNull(response.getEntity());

        TelemetriaResponseDto result = (TelemetriaResponseDto) response.getEntity();

        assertEquals(2, result.servicos().size());

        ServicosTelemetriaDto s1 = result.servicos().getFirst();
        assertEquals("Simulação", s1.getNome());
        assertEquals(10, s1.getQuantidadeChamadas());
        assertEquals(BigDecimal.valueOf(120), s1.getMediaTempoRespostaMs());

        ServicosTelemetriaDto s2 = result.servicos().get(1);
        assertEquals("Investimento", s2.getNome());
        assertEquals(5, s2.getQuantidadeChamadas());
        assertEquals(BigDecimal.valueOf(80), s2.getMediaTempoRespostaMs());

        PeriodoDto periodo = result.periodo();
        assertEquals(LocalDate.of(2024, 9, 25), periodo.getInicio());
        assertEquals(LocalDate.of(2024, 10, 20), periodo.getFim());

        verify(telemetriaDao, times(1)).listarTelemetrias();
    }
}

package br.com.caixa.desafio.unitarios.facade;

import br.com.caixa.desafio.dao.SimulacaoDao;
import br.com.caixa.desafio.dto.SimulacoesProdutoDiaDto;
import br.com.caixa.desafio.facade.ConsultarSimulacoesProdutoDiaFacade;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class ConsultarSimulacoesProdutoDiaFacadeTest {

    @Mock
    private SimulacaoDao simulacaoDao;

    @InjectMocks
    private ConsultarSimulacoesProdutoDiaFacade facade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveRetornarOkQuandoExistiremSimulacoes() {
        SimulacoesProdutoDiaDto simulacao = new SimulacoesProdutoDiaDto("Produto X", LocalDate.now(), 10L, BigDecimal.valueOf(100));
        when(simulacaoDao.simulacoesProdutoDia())
                .thenReturn(List.of(simulacao));

        Response response = facade.executar();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        List<?> body = (List<?>) response.getEntity();
        assertNotNull(body);
        assertEquals(1, body.size());
        verify(simulacaoDao, times(1)).simulacoesProdutoDia();
    }

    @Test
    void deveRetornarNoContentQuandoListaVazia() {
        when(simulacaoDao.simulacoesProdutoDia())
                .thenReturn(Collections.emptyList());

        Response response = facade.executar();

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        assertEquals("Simulacoes por dia nao encontradas no sistema", response.getEntity());
    }

    @Test
    void deveRetornarNoContentQuandoListaForNull() {
        when(simulacaoDao.simulacoesProdutoDia()).thenReturn(null);

        Response response = facade.executar();

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        assertEquals("Simulacoes por dia nao encontradas no sistema", response.getEntity());
    }
}

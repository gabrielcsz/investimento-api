package br.com.caixa.desafio.unitarios.facade;

import br.com.caixa.desafio.dao.SimulacaoDao;
import br.com.caixa.desafio.dto.ListaSimulacaoResponseDto;
import br.com.caixa.desafio.facade.ListarSimulacoes;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class ListarSimulacoesTest {

    @Mock
    private SimulacaoDao simulacaoDao;

    @InjectMocks
    private ListarSimulacoes facade;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveRetornarOkQuandoExistiremSimulacoes() {

        ListaSimulacaoResponseDto dto = ListaSimulacaoResponseDto.builder()
                .id(1L)
                .produto("CDB")
                .valorInvestido(BigDecimal.valueOf(1000.0))
                .valorFinal(BigDecimal.valueOf(1100.0))
                .dataSimulacao(LocalDateTime.now())
                .build();

        when(simulacaoDao.listarSimulacoes()).thenReturn(List.of(dto));

        Response response = facade.executar();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertNotNull(response.getEntity());

        List<?> lista = (List<?>) response.getEntity();
        assertEquals(1, lista.size());

        verify(simulacaoDao, times(1)).listarSimulacoes();
    }

    @Test
    void deveRetornarNoContentQuandoListaVazia() {
        when(simulacaoDao.listarSimulacoes()).thenReturn(Collections.emptyList());

        Response response = facade.executar();

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        assertEquals("Simulacoes nao encontradas no sistema", response.getEntity());

        verify(simulacaoDao, times(1)).listarSimulacoes();
    }

    @Test
    void deveRetornarNoContentQuandoListaForNull() {
        when(simulacaoDao.listarSimulacoes()).thenReturn(null);

        Response response = facade.executar();

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        assertEquals("Simulacoes nao encontradas no sistema", response.getEntity());

        verify(simulacaoDao, times(1)).listarSimulacoes();
    }
}

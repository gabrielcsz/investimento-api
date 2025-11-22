package br.com.caixa.desafio.unitarios.facade;

import br.com.caixa.desafio.dao.InvestimentoDao;
import br.com.caixa.desafio.dto.InvestimentoResponseDto;
import br.com.caixa.desafio.facade.InvestimentoClienteFacade;
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

class InvestimentoClienteFacadeTest {

    @Mock
    private InvestimentoDao investimentoDao;

    @InjectMocks
    private InvestimentoClienteFacade facade;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveRetornarOkQuandoExistemInvestimentos() {
        Long clienteId = 123L;

        InvestimentoResponseDto investimento =
                new InvestimentoResponseDto(1L,"CDB", BigDecimal.valueOf(1000.0), BigDecimal.valueOf(0.12), LocalDate.now());

        when(investimentoDao.listaInvestimentoCliente(clienteId))
                .thenReturn(List.of(investimento));

        Response response = facade.executar(clienteId);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertNotNull(response.getEntity());

        List<?> lista = (List<?>) response.getEntity();
        assertEquals(1, lista.size());

        verify(investimentoDao, times(1)).listaInvestimentoCliente(clienteId);
    }

    @Test
    void deveRetornarNoContentQuandoListaVazia() {
        Long clienteId = 10L;

        when(investimentoDao.listaInvestimentoCliente(clienteId))
                .thenReturn(Collections.emptyList());

        Response response = facade.executar(clienteId);

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        assertEquals("Cliente com ID 10 nao possui investimentos", response.getEntity());

        verify(investimentoDao, times(1)).listaInvestimentoCliente(clienteId);
    }

    @Test
    void deveRetornarNoContentQuandoListaForNull() {
        Long clienteId = 99L;

        when(investimentoDao.listaInvestimentoCliente(clienteId)).thenReturn(null);

        Response response = facade.executar(clienteId);

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        assertEquals("Cliente com ID 99 nao possui investimentos", response.getEntity());

        verify(investimentoDao, times(1)).listaInvestimentoCliente(clienteId);
    }
}

package br.com.caixa.desafio.unitarios.facade;

import br.com.caixa.desafio.dao.ProdutoDao;
import br.com.caixa.desafio.dao.SimulacaoDao;
import br.com.caixa.desafio.dto.ProdutoValidadoDto;
import br.com.caixa.desafio.dto.SimularRequestDto;
import br.com.caixa.desafio.dto.SimularResponseDto;
import br.com.caixa.desafio.entity.SimulacaoEntity;
import br.com.caixa.desafio.exceptions.CaixaVersoBOException;
import br.com.caixa.desafio.facade.CriarSimulacaoFacade;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CriarSimulacaoFacadeTest {

    @Mock
    ProdutoDao produtoDao;

    @Mock
    SimulacaoDao simulacaoDao;

    @InjectMocks
    CriarSimulacaoFacade facade;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCriarSimulacaoComSucesso() {

        SimularRequestDto request = SimularRequestDto.builder()
                .clienteId(123L)
                .tipoProduto("CDB")
                .valor(BigDecimal.valueOf(1000))
                .prazoMeses(12)
                .build();

        ProdutoValidadoDto produtoValidado = ProdutoValidadoDto.builder()
                .id(10L)
                .tipo("CDB")
                .rentabilidade(BigDecimal.valueOf(0.10))
                .build();

        when(produtoDao.buscarProdutoPorTipo("CDB")).thenReturn(produtoValidado);

        Response response = facade.executar(request);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertNotNull(response.getEntity());

        SimularResponseDto body = (SimularResponseDto) response.getEntity();

        assertEquals(BigDecimal.valueOf(0.10), body.getResultadoSimulacao().getRentabilidadeEfetiva());
        assertEquals(12, body.getResultadoSimulacao().getPrazoMeses());
        assertEquals(BigDecimal.valueOf(1100.00).stripTrailingZeros(),
                body.getResultadoSimulacao().getValorFinal().stripTrailingZeros());

        verify(simulacaoDao, times(1)).salvarSimulacao(any(SimulacaoEntity.class));
        verify(produtoDao, times(1)).buscarProdutoPorTipo("CDB");
    }

    @Test
    void deveLancarExcecaoQuandoProdutoNaoEncontrado() {
        SimularRequestDto request = SimularRequestDto.builder()
                .tipoProduto("TESOURO")
                .valor(BigDecimal.valueOf(500))
                .prazoMeses(6)
                .clienteId(777L)
                .build();

        when(produtoDao.buscarProdutoPorTipo("TESOURO")).thenReturn(null);

        CaixaVersoBOException exception =
                assertThrows(CaixaVersoBOException.class, () -> facade.executar(request));

        assertEquals("Produto nao encontrado", exception.getTitle());
        assertEquals(400, exception.getStatus());
        assertEquals("Tipo de produto inválido", exception.getDetail());

        verify(simulacaoDao, never()).salvarSimulacao(any());
    }
}

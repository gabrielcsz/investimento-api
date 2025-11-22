package br.com.caixa.desafio.unitarios.facade;

import br.com.caixa.desafio.bo.ProdutoRecomendacaoBO;
import br.com.caixa.desafio.dao.ProdutoDao;
import br.com.caixa.desafio.dto.ProdutoValidadoDto;
import br.com.caixa.desafio.entity.ProdutoEntity;
import br.com.caixa.desafio.exceptions.CaixaVersoBOException;
import br.com.caixa.desafio.facade.RecomendarProdutoFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class RecomendarProdutoFacadeTest {

    @InjectMocks
    RecomendarProdutoFacade facade;

    @Mock
    ProdutoDao produtoDao;

    @BeforeEach
    void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
    }

    static {
        mockStatic(ProdutoRecomendacaoBO.class);
    }

    @Test
    void deveLancarExcecaoQuandoPerfilInvalido() {
        assertThrows(
                CaixaVersoBOException.class,
                () -> facade.executar("xxxx")
        );
    }

    @Test
    void deveRetornarProdutosParaPerfilConservador() {
        ProdutoEntity p1 = new ProdutoEntity();
        p1.setId(1L);
        p1.setNome("Produto A");
        p1.setRisco("baixo");
        p1.setRentabilidade(BigDecimal.valueOf(0.2));

        ProdutoEntity p2 = new ProdutoEntity();
        p2.setId(2L);
        p2.setNome("Produto B");
        p2.setRisco("medio");
        p2.setRentabilidade(BigDecimal.valueOf(0.4));

        when(produtoDao.listarTodosProdutos()).thenReturn(List.of(p1, p2));

        when(ProdutoRecomendacaoBO.calcularPontos(any(), anyDouble(), anyDouble(), anyDouble()))
                .thenAnswer(inv -> {

                    ProdutoEntity prod = inv.getArgument(0);

                    return switch (prod.getNome()) {
                        case "Produto A" -> 4.5;
                        case "Produto B" -> 1.2;
                        default -> 0.0;
                    };
                });

        var resp = facade.executar("conservador");
        List<ProdutoValidadoDto> list = (List<ProdutoValidadoDto>) resp.getEntity();

        assertEquals(1, list.size());
        assertEquals(1L, list.get(0).getId());
        assertEquals("Produto A", list.get(0).getNome());
    }

    @Test
    void deveOrdenarProdutosPorPontuacao() {

        ProdutoEntity p1 = new ProdutoEntity();
        p1.setId(1L);
        p1.setNome("Produto A");

        ProdutoEntity p2 = new ProdutoEntity();
        p2.setId(2L);
        p2.setNome("Produto B");

        ProdutoEntity p3 = new ProdutoEntity();
        p3.setId(3L);
        p3.setNome("Produto C");

        when(produtoDao.listarTodosProdutos()).thenReturn(List.of(p1, p2, p3));

        when(ProdutoRecomendacaoBO.calcularPontos(eq(p1), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(3.5);
        when(ProdutoRecomendacaoBO.calcularPontos(eq(p2), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(6.0);
        when(ProdutoRecomendacaoBO.calcularPontos(eq(p3), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(4.0);

        var resp = facade.executar("moderado");
        var lista = (List<ProdutoValidadoDto>) resp.getEntity();

        assertEquals(3, lista.size());

        assertEquals("Produto B", lista.get(0).getNome());
        assertEquals("Produto C", lista.get(1).getNome());
        assertEquals("Produto A", lista.get(2).getNome());
    }

    @Test
    void deveFiltrarPorScoreMinimo() {

        ProdutoEntity p1 = new ProdutoEntity();
        p1.setId(1L);
        p1.setNome("A");

        ProdutoEntity p2 = new ProdutoEntity();
        p2.setId(2L);
        p2.setNome("B");

        when(produtoDao.listarTodosProdutos()).thenReturn(List.of(p1, p2));

        when(ProdutoRecomendacaoBO.calcularPontos(eq(p1), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(1.5);

        when(ProdutoRecomendacaoBO.calcularPontos(eq(p2), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(3.0);

        var resp = facade.executar("agressivo");
        var lista = (List<ProdutoValidadoDto>) resp.getEntity();

        assertEquals(1, lista.size());
        assertEquals("B", lista.get(0).getNome());
    }
}

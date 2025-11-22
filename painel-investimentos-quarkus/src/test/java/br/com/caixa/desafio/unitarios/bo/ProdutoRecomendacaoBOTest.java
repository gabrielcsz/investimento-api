package br.com.caixa.desafio.unitarios.bo;

import br.com.caixa.desafio.bo.ProdutoRecomendacaoBO;
import br.com.caixa.desafio.entity.ProdutoEntity;
import br.com.caixa.desafio.enums.PreferenciaLiquidezEnum;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProdutoRecomendacaoBOTest {

    @Test
    void testCalcularPontos_ComValoresPadrao() {
        ProdutoEntity produto = new ProdutoEntity();
        produto.setRisco("baixo");
        produto.setLiquidez(PreferenciaLiquidezEnum.RENTABILIDADE.getId());
        produto.setRentabilidade(BigDecimal.valueOf(0.15));

        double resultado = ProdutoRecomendacaoBO.calcularPontos(produto,
                1.0, 1.0, 1.0);

        assertEquals(9.0, resultado);
    }

    @Test
    void testRiscoMedio() {
        ProdutoEntity produto = new ProdutoEntity();
        produto.setRisco("medio");
        produto.setLiquidez(PreferenciaLiquidezEnum.LIQUIDEZ.getId());
        produto.setRentabilidade(BigDecimal.valueOf(0.05));

        double resultado = ProdutoRecomendacaoBO.calcularPontos(produto,
                1.0, 0.0, 0.0);

        assertEquals(2.0, resultado);
    }

    @Test
    void testRiscoAlto() {
        ProdutoEntity produto = new ProdutoEntity();
        produto.setRisco("alto");
        produto.setLiquidez(PreferenciaLiquidezEnum.LIQUIDEZ.getId());
        produto.setRentabilidade(BigDecimal.valueOf(0.05));

        double resultado = ProdutoRecomendacaoBO.calcularPontos(produto,
                1.0, 0.0, 0.0);

        assertEquals(1.0, resultado);
    }

    @Test
    void testLiquidezEquilibrio() {
        ProdutoEntity produto = new ProdutoEntity();
        produto.setRisco("baixo");
        produto.setLiquidez(PreferenciaLiquidezEnum.EQUILIBRIO.getId());
        produto.setRentabilidade(BigDecimal.valueOf(0.05));

        double resultado = ProdutoRecomendacaoBO.calcularPontos(produto,
                0.0, 1.0, 0.0);

        assertEquals(2.0, resultado);
    }

    @Test
    void testRentabilidadeAlta() {
        ProdutoEntity produto = new ProdutoEntity();
        produto.setRisco("baixo");
        produto.setLiquidez(PreferenciaLiquidezEnum.RENTABILIDADE.getId());
        produto.setRentabilidade(BigDecimal.valueOf(0.20));

        double resultado = ProdutoRecomendacaoBO.calcularPontos(produto,
                0.0, 0.0, 1.0);

        assertEquals(3.0, resultado);
    }

    @Test
    void testRentabilidadeMedia() {
        ProdutoEntity produto = new ProdutoEntity();
        produto.setRisco("baixo");
        produto.setLiquidez(PreferenciaLiquidezEnum.RENTABILIDADE.getId());
        produto.setRentabilidade(BigDecimal.valueOf(0.12));

        double resultado = ProdutoRecomendacaoBO.calcularPontos(produto,
                0.0, 0.0, 1.0);

        assertEquals(2.0, resultado);
    }

    @Test
    void testRentabilidadeBaixa() {
        ProdutoEntity produto = new ProdutoEntity();
        produto.setRisco("baixo");
        produto.setLiquidez(PreferenciaLiquidezEnum.RENTABILIDADE.getId());
        produto.setRentabilidade(BigDecimal.valueOf(0.05));

        double resultado = ProdutoRecomendacaoBO.calcularPontos(produto,
                0.0, 0.0, 1.0);

        assertEquals(1.0, resultado);
    }

    @Test
    void testCalculoComPesosPersonalizados() {
        ProdutoEntity produto = new ProdutoEntity();
        produto.setRisco("médio");
        produto.setLiquidez(PreferenciaLiquidezEnum.EQUILIBRIO.getId());
        produto.setRentabilidade(BigDecimal.valueOf(0.10));

        double resultado = ProdutoRecomendacaoBO.calcularPontos(produto,
                2.0,
                3.0,
                4.0
        );
        assertEquals(18.0, resultado);
    }
}

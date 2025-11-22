package br.com.caixa.desafio.bo;

import br.com.caixa.desafio.entity.ProdutoEntity;
import br.com.caixa.desafio.enums.PreferenciaLiquidezEnum;

import java.math.BigDecimal;

public class ProdutoRecomendacaoBO {
    public static double calcularPontos(ProdutoEntity produto, double risco, double liquidez, double rentabilidade) {
        return risco * riscoScore(produto.getRisco()) +
                liquidez * liquidezScore(PreferenciaLiquidezEnum.fromId(produto.getLiquidez())) +
                rentabilidade * rentabilidadeScore(produto.getRentabilidade());
    }

    private static int riscoScore(String risco) {
        return switch (risco.toLowerCase()) {
            case "baixo" -> 3;
            case "médio", "medio" -> 2;
            case "alto" -> 1;
            default -> 0;
        };
    }

    private static int liquidezScore(PreferenciaLiquidezEnum liquidez) {
        return switch (liquidez) {
            case RENTABILIDADE -> 3;
            case EQUILIBRIO -> 2;
            case LIQUIDEZ -> 1;
            default -> 0;
        };
    }

    private static int rentabilidadeScore(BigDecimal rentabilidade) {
        double r = rentabilidade.doubleValue();
        return (r >= 0.15) ? 3 : (r >= 0.10) ? 2 : 1;
    }
}

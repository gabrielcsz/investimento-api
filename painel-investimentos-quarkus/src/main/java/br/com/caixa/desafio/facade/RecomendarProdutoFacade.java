package br.com.caixa.desafio.facade;

import br.com.caixa.desafio.bo.ProdutoRecomendacaoBO;
import br.com.caixa.desafio.dao.ProdutoDao;
import br.com.caixa.desafio.dto.ProdutoValidadoDto;
import br.com.caixa.desafio.entity.ProdutoEntity;
import br.com.caixa.desafio.enums.PerfilEnum;
import br.com.caixa.desafio.exceptions.CaixaVersoBOException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class RecomendarProdutoFacade {

    @Inject
    ProdutoDao produtoDao;

    public Response executar(String perfil) {

        double risco, liquidez, rentabilidade, score=2.0;

        switch (PerfilEnum.fromString(perfil.toUpperCase())) {
            case CONSERVADOR -> {
                risco = 0.6;
                liquidez = 0.3;
                rentabilidade = 0.1;
            }
            case MODERADO -> {
                risco = 0.4;
                liquidez = 0.3;
                rentabilidade = 0.3;
            }
            case AGRESSIVO-> {
                risco = 0.1;
                liquidez = 0.2;
                rentabilidade = 0.7;
            }
            default -> throw new CaixaVersoBOException("Perfil desconhecido: " + perfil,404,"Perfil informado é inválido");
        }

        var produtosRecomendados = produtoDao.listarTodosProdutos().stream()
                .map(produto-> new ProdutoPontos(produto, ProdutoRecomendacaoBO.calcularPontos(produto, risco, liquidez, rentabilidade)))
                .filter(produtoPontos -> produtoPontos.pontos() > score)
                .sorted(Comparator.comparingDouble(ProdutoPontos::pontos).reversed())
                .map(ProdutoPontos::produto)
                .collect(Collectors.toList());

        List<ProdutoValidadoDto> response = produtosRecomendados.stream()
                .map(produto -> ProdutoValidadoDto.builder()
                        .id(produto.getId())
                        .tipo(produto.getTipo())
                        .nome(produto.getNome())
                        .risco(produto.getRisco())
                        .rentabilidade(produto.getRentabilidade())
                        .build()
                )
                .collect(Collectors.toList());

        return Response.ok(response).build();
    }

    private record ProdutoPontos(ProdutoEntity produto, double pontos) {}
}

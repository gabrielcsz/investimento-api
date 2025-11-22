package br.com.caixa.desafio.facade;

import br.com.caixa.desafio.dao.ProdutoDao;
import br.com.caixa.desafio.dao.SimulacaoDao;
import br.com.caixa.desafio.dto.ProdutoValidadoDto;
import br.com.caixa.desafio.dto.ResultadoSimulacaoDto;
import br.com.caixa.desafio.dto.SimularRequestDto;
import br.com.caixa.desafio.dto.SimularResponseDto;
import br.com.caixa.desafio.entity.SimulacaoEntity;
import br.com.caixa.desafio.exceptions.CaixaVersoBOException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@ApplicationScoped
public class CriarSimulacaoFacade {

    @Inject
    ProdutoDao produtoDao;

    @Inject
    SimulacaoDao simulacaoDao;

    public Response executar(SimularRequestDto requestDto){
        ProdutoValidadoDto produtoValidado = recuperarProduto(requestDto.getTipoProduto());

        BigDecimal taxa = produtoValidado.getRentabilidade();
        BigDecimal valorFinal = requestDto.getValor().multiply(BigDecimal.ONE.add(taxa));


        ResultadoSimulacaoDto resultadoSimulacaoDTO = ResultadoSimulacaoDto
                .builder()
                .rentabilidadeEfetiva(taxa)
                .prazoMeses(requestDto.getPrazoMeses())
                .valorFinal(valorFinal)
                .build();

        SimularResponseDto response = SimularResponseDto
                .builder()
                .produtoValidado(produtoValidado)
                .resultadoSimulacao(resultadoSimulacaoDTO)
                .dataSimulacao(LocalDateTime.now())
                .build();

        SimulacaoEntity simulacaoEntity = SimulacaoEntity
                .builder()
                .clientId(requestDto.getClienteId())
                .idProduto(produtoValidado.getId())
                .valorInvestido(requestDto.getValor())
                .valorFinal(valorFinal)
                .prazoMeses(requestDto.getPrazoMeses())
                .dataSimulacao(response.getDataSimulacao())
                .build();

        simulacaoDao.salvarSimulacao(simulacaoEntity);

        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    private ProdutoValidadoDto recuperarProduto(String tipoProduto) {
        var produto = produtoDao.buscarProdutoPorTipo(tipoProduto);
        if(produto == null){
            throw new CaixaVersoBOException("Produto nao encontrado",400,"Tipo de produto inválido");
        } else {
            return produto;
        }
    }
}

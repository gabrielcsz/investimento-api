package br.com.caixa.desafio.facade;

import br.com.caixa.desafio.dao.InvestimentoDao;
import br.com.caixa.desafio.dto.InvestimentoResponseDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

import java.util.List;

@ApplicationScoped
public class InvestimentoClienteFacade {

    @Inject
    InvestimentoDao investimentoDao;

    public Response executar(Long clienteId){
        List<InvestimentoResponseDto> investimentoList = investimentoDao.listaInvestimentoCliente(clienteId);
        if (investimentoList == null || investimentoList.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT)
                    .entity("Cliente com ID " + clienteId + " nao possui investimentos").build();
        }
        return Response.ok(investimentoList).build();
    }
}

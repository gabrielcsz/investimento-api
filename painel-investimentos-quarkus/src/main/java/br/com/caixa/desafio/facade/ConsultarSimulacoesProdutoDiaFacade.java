package br.com.caixa.desafio.facade;

import br.com.caixa.desafio.dao.SimulacaoDao;
import br.com.caixa.desafio.dto.SimulacoesProdutoDiaDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

import java.util.List;

@ApplicationScoped
public class ConsultarSimulacoesProdutoDiaFacade {

    @Inject
    SimulacaoDao simulacaoDao;

    public Response executar() {

        List<SimulacoesProdutoDiaDto> response = simulacaoDao.simulacoesProdutoDia();
        if(response == null || response.isEmpty()){
            return Response.status(Response.Status.NO_CONTENT).entity("Simulacoes por dia nao encontradas no sistema").build();
        }
        return Response.ok(response).build();
    }
}

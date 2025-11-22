package br.com.caixa.desafio.facade;

import br.com.caixa.desafio.dao.SimulacaoDao;
import br.com.caixa.desafio.dto.ListaSimulacaoResponseDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

import java.util.List;

@ApplicationScoped
public class ListarSimulacoes {

    @Inject
    SimulacaoDao simulacaoDao;

    public Response executar(){

        List<ListaSimulacaoResponseDto> response = simulacaoDao.listarSimulacoes();
        if(response == null || response.isEmpty()){
            return Response.status(Response.Status.NO_CONTENT).entity("Simulacoes nao encontradas no sistema").build();
        }
        return Response.ok(response).build();
    }
}

package br.com.caixa.desafio.rest;

import br.com.caixa.desafio.annotation.NomeServico;
import br.com.caixa.desafio.dto.ListaSimulacaoResponseDto;
import br.com.caixa.desafio.dto.SimulacoesProdutoDiaDto;
import br.com.caixa.desafio.dto.SimularRequestDto;
import br.com.caixa.desafio.dto.SimularResponseDto;
import br.com.caixa.desafio.exceptions.ErrorResponse;
import br.com.caixa.desafio.facade.ConsultarSimulacoesProdutoDiaFacade;
import br.com.caixa.desafio.facade.CriarSimulacaoFacade;
import br.com.caixa.desafio.facade.ListarSimulacoes;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Slf4j
@Path("/simulacoes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Simulações", description = "Operações relacionadas às simulações de investimentos")
public class SimulacaoRest {

    @Inject
    CriarSimulacaoFacade criarSimulacaoFacade;

    @Inject
    ListarSimulacoes listarSimulacoes;

    @Inject
    ConsultarSimulacoesProdutoDiaFacade consultarSimulacoesProdutoDia;

    @POST
    @Path("/simular-investimento")
    @NomeServico("simular-investimento")
    @Operation(
            summary = "Realiza uma simulação de investimento",
            description = "Recebe os dados da simulação e retorna o cálculo completo"
    )
    @APIResponse(
            responseCode = "200",
            description = "Simulação realizada com sucesso",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = SimularResponseDto.class))
    )
    @APIResponse(
            responseCode = "400",
            description = "Requisição inválida",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = {
                            @ExampleObject(
                                    name = "Cliente inválido",
                                    value = """
                                        {
                                            "title": "Requisição inválida",
                                            "status": 400,
                                            "detail": "O ID do cliente não pode ser negativo"
                                        }
                                        """
                            )
                    }
            )
    )
    @APIResponse(
            responseCode = "404",
            description = "Cliente não encontrado",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = {
                            @ExampleObject(
                                    name = "Cliente inexistente",
                                    value = """
                                        {
                                            "title": "Não encontrado",
                                            "status": 404,
                                            "detail": "Nenhum cliente foi encontrado com o ID informado"
                                        }
                                        """
                            )
                    }
            )
    )
    @APIResponse(
            responseCode = "500",
            description = "Erro inesperado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public Response simularInvestimento(@Valid SimularRequestDto requestDto){
        return criarSimulacaoFacade.executar(requestDto);
    }

    @GET
    @NomeServico("listar-todas-simulacoes")
    @Operation(
            summary = "Lista todas as simulações registradas",
            description = "Retorna uma lista de simulações realizadas pelo sistema"
    )
    @APIResponse(
            responseCode = "200",
            description = "Lista de simulações",
            content = @Content(schema = @Schema(implementation = ListaSimulacaoResponseDto.class))
    )
    public Response listarTodasSimulacoes(){
        return listarSimulacoes.executar();
    }

    @GET
    @Path("/por-produto-dia")
    @NomeServico("simulacoes-por-produto-dia")
    @Operation(
            summary = "Agrupa simulações por produto e por dia",
            description = "Retorna estatísticas diárias das simulações"
    )
    @APIResponse(
            responseCode = "200",
            description = "Lista agrupada",
            content = @Content(schema = @Schema(implementation = SimulacoesProdutoDiaDto.class))
    )
    public Response simulacoesProdutoDia(){
        return consultarSimulacoesProdutoDia.executar();
    }
}

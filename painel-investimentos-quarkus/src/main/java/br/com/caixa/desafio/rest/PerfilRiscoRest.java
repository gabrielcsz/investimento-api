package br.com.caixa.desafio.rest;

import br.com.caixa.desafio.annotation.NomeServico;
import br.com.caixa.desafio.dto.PerfilRiscoResponseDto;
import br.com.caixa.desafio.exceptions.ErrorResponse;
import br.com.caixa.desafio.facade.PerfilRiscoFacade;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/perfil-risco")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Perfil de Risco", description = "Consulta o perfil de risco do cliente")
public class PerfilRiscoRest {

    @Inject
    PerfilRiscoFacade perfilRiscoFacade;

    @GET
    @Path("/{clienteId}")
    @NomeServico("perfil-risco")
    @Operation(
            summary = "Consulta o perfil de risco do cliente",
            description = "Realiza o cálculo do perfil de risco com base nos dados do cliente."
    )
    @APIResponse(
            responseCode = "200",
            description = "Perfil calculado com sucesso",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = PerfilRiscoResponseDto.class)
            )
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
            description = "Erro interno inesperado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public Response perfilRisco(@PathParam("clienteId") Long clienteId){
        return perfilRiscoFacade.executar(clienteId);
    }
}

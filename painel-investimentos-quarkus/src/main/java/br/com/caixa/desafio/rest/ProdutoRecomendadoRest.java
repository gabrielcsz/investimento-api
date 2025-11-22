package br.com.caixa.desafio.rest;


import br.com.caixa.desafio.annotation.NomeServico;
import br.com.caixa.desafio.dto.ProdutoValidadoDto;
import br.com.caixa.desafio.exceptions.ErrorResponse;
import br.com.caixa.desafio.facade.RecomendarProdutoFacade;
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

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/produtos-recomendados")
@Tag(name = "Recomendações", description = "Recomenda produtos com base no perfil do cliente")
public class ProdutoRecomendadoRest {

    @Inject
    RecomendarProdutoFacade recomendarProdutoFacade;

    @GET
    @Path("/{perfil}")
    @NomeServico("produtos-recomendados")
    @Operation(
            summary = "Lista produtos recomendados",
            description = "Retorna uma recomendação de produtos de investimento com base no perfil informado (e.g., CONSERVADOR, MODERADO, ARROJADO)."
    )
    @APIResponse(
            responseCode = "200",
            description = "Lista de produtos recomendados retornada com sucesso",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ProdutoValidadoDto.class)
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
            responseCode = "500",
            description = "Erro inesperado no sistema",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public Response produtosRecomendados(@PathParam("perfil") String perfil) {
        return recomendarProdutoFacade.executar(perfil);
    }
}

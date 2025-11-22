package br.com.caixa.desafio.rest;

import br.com.caixa.desafio.annotation.NomeServico;
import br.com.caixa.desafio.dto.TelemetriaResponseDto;
import br.com.caixa.desafio.facade.ObterTelemetriaFacade;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/telemetria")
@Tag(name = "Telemetria", description = "Obtenção de dados de telemetria dos serviços")
public class TelemetriaRest {

    @Inject
    ObterTelemetriaFacade obterTelemetriaFacade;

    @GET
    @NomeServico("/telemetria")
    @Operation(
            summary = "Obter telemetria dos serviços",
            description = "Retorna informações consolidadas de telemetria dos serviços, incluindo estatísticas e período analisado."
    )
    @APIResponse(
            responseCode = "200",
            description = "Dados retornados com sucesso",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = TelemetriaResponseDto.class)
            )
    )
    @APIResponse(
            responseCode = "500",
            description = "Erro interno ao processar telemetria"
    )
    public Response obterTelemetria(){
        return obterTelemetriaFacade.executar();
    }
}

package br.com.caixa.desafio.dto;

import br.com.caixa.desafio.enums.PerfilEnum;
import lombok.Builder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Builder
@Schema(name = "PerfilRiscoResponseDto", description = "Retorno do perfil de risco do cliente")
public record PerfilRiscoResponseDto(

        @Schema(description = "ID do cliente", example = "123")
        Long clienteId,

        @Schema(description = "Perfil de risco calculado", implementation = PerfilEnum.class, example = "MODERADO")
        PerfilEnum perfil,

        @Schema(description = "Pontuação total do cliente", example = "75")
        int pontuacao,

        @Schema(description = "Descrição do perfil de risco", example = "Cliente com tolerância moderada ao risco")
        String descricao

) {}

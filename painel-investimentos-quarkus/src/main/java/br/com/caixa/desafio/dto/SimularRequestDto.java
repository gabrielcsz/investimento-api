package br.com.caixa.desafio.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "SimularRequestDto",
        description = "Dados utilizados para realizar uma simulação de investimento"
)
public class SimularRequestDto {

    @Schema(
            description = "Identificador do cliente que está realizando a simulação",
            example = "123",
            required = true
    )
    @NotNull(message = "O campo 'clienteId' não pode ser nulo")
    @Positive(message = "O campo 'clienteId' deve ser positivo")
    private Long clienteId;

    @Schema(
            description = "Valor que será aplicado no investimento",
            example = "15000.00",
            required = true
    )
    @NotNull(message = "O campo 'valor' não pode ser nulo")
    @Positive(message = "O campo 'valor' deve ser positivo")
    private BigDecimal valor;

    @Schema(
            description = "Prazo do investimento em meses",
            example = "12",
            required = true
    )
    @NotNull(message = "O campo 'prazoMeses' não pode ser nulo")
    @Positive(message = "O campo 'prazoMeses' deve ser positivo")
    private Integer prazoMeses;

    @Schema(
            description = "Tipo do produto de investimento (CDB, LCI, LCA, FUNDOS, etc.)",
            example = "CDB",
            required = true
    )
    @NotNull(message = "O campo 'tipoProduto' não pode ser nulo")
    private String tipoProduto;
}

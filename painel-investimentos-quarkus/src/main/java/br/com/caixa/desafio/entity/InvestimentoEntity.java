package br.com.caixa.desafio.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "INVESTIMENTO", schema = "dbo")
public class InvestimentoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "INVESTIMENTO_ID")
    private Long id;

    @Column(name = "INVESTIMENTO_CLIENTE_ID", nullable = false)
    private Long clienteId;

    @Column(name = "INVESTIMENTO_PRODUTO_ID", nullable = false)
    private Long produtoId;

    @Column(name = "INVESTIMENTO_VALOR_APLICADO", precision = 18, scale = 2, nullable = false)
    private BigDecimal valorAplicado;

    @Column(name = "INVESTIMENTO_DATA_APLICACAO", nullable = false)
    private LocalDateTime dataAplicacao;

    @Column(name = "INVESTIMENTO_DATA_RESGATE")
    private LocalDateTime dataResgate;
}

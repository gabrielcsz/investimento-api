package br.com.caixa.desafio.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "SIMULACAO", schema = "dbo")
public class SimulacaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SIMULACAO_ID")
    private Long id;

    @Column(name = "SIMULACAO_CLIENT_ID")
    private Long clientId;

    @Column(name = "SIMULACAO_PRODUTO_ID")
    private Long idProduto;

    @Column(name = "SIMULACAO_VALOR_INVESTIDO", precision = 12, scale = 2)
    private BigDecimal valorInvestido;

    @Column(name = "SIMULACAO_VALOR_FINAL", precision = 12, scale = 2)
    private BigDecimal valorFinal;

    @Column(name = "SIMULACAO_PRAZO_MESES")
    private Integer prazoMeses;

    @Column(name = "SIMULACAO_DATA_SIMULACAO")
    private LocalDateTime dataSimulacao;
}

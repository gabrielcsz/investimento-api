package br.com.caixa.desafio.entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TELEMETRIA", schema = "dbo")
public class TelemetriaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TELEMETRIA_ID")
    private Long id;

    @Column(name = "TELEMETRIA_SERVICO")
    private String servico;

    @Column(name = "TELEMETRIA_QUANTIDADE_CHAMADAS")
    private Integer quantidadeChamadas;

    @Column(name = "TELEMETRIA_MEDIA_TEMPO_RESPOSTA", precision = 10, scale = 2)
    private BigDecimal mediaTempoResposta;

    @Column(name = "TELEMETRIA_PERIODO_INICIO")
    private LocalDate periodoInicio;

    @Column(name = "TELEMETRIA_PERIODO_FIM")
    private LocalDate periodoFim;

}

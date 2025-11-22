package br.com.caixa.desafio.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "CLIENTE", schema = "dbo")
public class ClienteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CLIENTE_ID")
    private Long id;

    @Column(name = "CLIENTE_NOME", nullable = false)
    private String nome;

    @Column(name = "CLIENTE_RENDA_MENSAL", precision = 18, scale = 2, nullable = false)
    private BigDecimal rendaMensal;

    @Column(name = "CLIENTE_PATRIMONIO", precision = 18, scale = 2, nullable = false)
    private BigDecimal patrimonio;

    @Column(name = "CLIENTE_PREFERENCIA_LIQUIDEZ", nullable = true)
    private Integer preferenciaLiquidez; // 0 = baixo, 1 = moderado, 2 = alta liquidez
}

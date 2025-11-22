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
@Table(name = "PRODUTO", schema = "dbo")
public class ProdutoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUTO_ID")
    private Long id;

    @Column(name = "PRODUTO_NOME")
    private String nome;

    @Column(name = "PRODUTO_TIPO")
    private String tipo;

    @Column(name = "PRODUTO_RENTABILIDADE", precision = 5, scale = 4)
    private BigDecimal rentabilidade;

    @Column(name = "PRODUTO_RISCO")
    private String risco;

    @Column(name = "PRODUTO_LIQUIDEZ")
    private Integer liquidez;
    //1 baixa, 2 média, 3 alta
}

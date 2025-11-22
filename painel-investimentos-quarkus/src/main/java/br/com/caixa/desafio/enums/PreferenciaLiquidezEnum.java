package br.com.caixa.desafio.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PreferenciaLiquidezEnum {

    LIQUIDEZ (1,"LIQUIDEZ"), EQUILIBRIO (2,"EQUILIBRIO"), RENTABILIDADE (3,"RENTABILIDADE");
    private final Integer id;
    private final String valor;

    public static PreferenciaLiquidezEnum fromId(Integer id) {
        for (PreferenciaLiquidezEnum preferencia : PreferenciaLiquidezEnum.values()) {
            if (preferencia.getId().equals(id)) {
                return preferencia;
            }
        }
        throw new IllegalArgumentException("Id de preferência inválido: " + id);
    }
}

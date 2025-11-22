package br.com.caixa.desafio.enums;

import br.com.caixa.desafio.exceptions.CaixaVersoBOException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PerfilEnum {
    CONSERVADOR("Perfil voltado à segurança e liquidez."),
    MODERADO("Perfil equilibrado entre segurança e rentabilidade."),
    AGRESSIVO("Perfil voltado à rentabilidade com maior tolerância a risco.");

    private final String descricao;

    public static PerfilEnum fromString(String perfil) {
        for (PerfilEnum p : PerfilEnum.values()) {
            if (p.name().equalsIgnoreCase(perfil)) {
                return p;
            }
        }
        throw new CaixaVersoBOException("Perfil desconhecido: " + perfil, 404, "Perfil informado é inválido");
    }

    public static String fromId(Integer liquidez) {
        for (PerfilEnum p : PerfilEnum.values()) {
            if (p.ordinal()  == liquidez) {
                return p.name();
            }
        }
        throw new IllegalArgumentException("Perfil desconhecido");

    }
}

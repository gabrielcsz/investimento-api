package br.com.caixa.desafio.facade;

import br.com.caixa.desafio.dao.ClienteDao;
import br.com.caixa.desafio.dao.InvestimentoDao;
import br.com.caixa.desafio.dto.PerfilRiscoResponseDto;
import br.com.caixa.desafio.entity.ClienteEntity;
import br.com.caixa.desafio.entity.InvestimentoEntity;
import br.com.caixa.desafio.enums.PerfilEnum;
import br.com.caixa.desafio.enums.PreferenciaLiquidezEnum;
import br.com.caixa.desafio.exceptions.CaixaVersoBOException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class PerfilRiscoFacade {

    @Inject
    InvestimentoDao investimentoDao;

    @Inject
    ClienteDao clienteDao;

    public Response executar(Long clienteId) {


        ClienteEntity cliente = clienteDao.buscarPorId(clienteId);
        if (cliente == null) {
            throw new CaixaVersoBOException("Cliente nao encontrado com ID: " + clienteId,
                    404, "Cliente com ID " + clienteId + " nao encontrado");
        }

        List<InvestimentoEntity> investimentosLista = investimentoDao.listaInvestimento(clienteId);
        if (investimentosLista == null || investimentosLista.isEmpty()) {
            throw new CaixaVersoBOException("Cliente nao possui investimentos: " + clienteId,
                    404, "Cliente com ID " + clienteId + " existe, mas nao possui investimentos");
        }

        BigDecimal volumeTotal = investimentosLista.stream()
                .map(inv -> inv.getValorAplicado())
                .reduce((v1,v2) -> v1.add(v2))
                .orElseThrow(()-> new RuntimeException("Erro ao calcular o volume total investido"));
        int pontuacao = 0;

        double volumeInvestido = volumeTotal.doubleValue();
        pontuacao += (volumeInvestido >= 20000) ? 40 :
                (volumeInvestido >= 10000) ? 20 : 10;

        PreferenciaLiquidezEnum preferencia = PreferenciaLiquidezEnum.fromId(cliente.getPreferenciaLiquidez());
        switch (preferencia) {
            case LIQUIDEZ -> pontuacao += 10;
            case RENTABILIDADE -> pontuacao += 30;
            case EQUILIBRIO -> pontuacao += 20;
            default -> pontuacao += 15;
        }

        int movimentacao = (int) investimentosLista.stream()
                .filter(inv -> inv.getDataAplicacao().toLocalDate().isAfter(LocalDate.now().minusDays(30)))
                .count();

        pontuacao += (movimentacao <=2) ? 10 :
                (movimentacao <=6) ? 20 : 30;

        PerfilEnum perfil;

        if (pontuacao < 40) {
            perfil = PerfilEnum.CONSERVADOR;
        } else if (pontuacao < 70) {
            perfil = PerfilEnum.MODERADO;
        } else {
            perfil = PerfilEnum.AGRESSIVO;
        }

        PerfilRiscoResponseDto response = PerfilRiscoResponseDto.builder()
                .clienteId(clienteId)
                .perfil(perfil)
                .pontuacao(pontuacao)
                .descricao(perfil.getDescricao())
                .build();

        return Response.ok().entity(response).build();
    }
}

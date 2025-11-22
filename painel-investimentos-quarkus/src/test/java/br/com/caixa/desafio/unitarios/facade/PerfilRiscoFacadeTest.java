package br.com.caixa.desafio.unitarios.facade;

import br.com.caixa.desafio.dao.ClienteDao;
import br.com.caixa.desafio.dao.InvestimentoDao;
import br.com.caixa.desafio.dto.PerfilRiscoResponseDto;
import br.com.caixa.desafio.entity.ClienteEntity;
import br.com.caixa.desafio.entity.InvestimentoEntity;
import br.com.caixa.desafio.enums.PerfilEnum;
import br.com.caixa.desafio.enums.PreferenciaLiquidezEnum;
import br.com.caixa.desafio.exceptions.CaixaVersoBOException;
import br.com.caixa.desafio.facade.PerfilRiscoFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;

class PerfilRiscoFacadeTest {

    @InjectMocks
    PerfilRiscoFacade facade;

    @Mock
    ClienteDao clienteDao;

    @Mock
    InvestimentoDao investimentoDao;

    @BeforeEach
    void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveLancarExcecaoQuandoClienteNaoExiste() {
        when(clienteDao.buscarPorId(1L)).thenReturn(null);

        CaixaVersoBOException ex = assertThrows(
                CaixaVersoBOException.class,
                () -> facade.executar(1L)
        );

        assertEquals(404, ex.getStatus());
    }

    @Test
    void deveLancarExcecaoQuandoClienteNaoTemInvestimentos() {

        ClienteEntity cliente = new ClienteEntity();
        cliente.setPreferenciaLiquidez(PreferenciaLiquidezEnum.LIQUIDEZ.getId());

        when(clienteDao.buscarPorId(1L)).thenReturn(cliente);
        when(investimentoDao.listaInvestimento(1L)).thenReturn(List.of());

        CaixaVersoBOException ex = assertThrows(
                CaixaVersoBOException.class,
                () -> facade.executar(1L)
        );

        assertEquals(404, ex.getStatus());
    }

    @Test
    void deveRetornarPerfilConservador() {
        ClienteEntity cliente = new ClienteEntity();
        cliente.setPreferenciaLiquidez(PreferenciaLiquidezEnum.LIQUIDEZ.getId());

        InvestimentoEntity inv = new InvestimentoEntity();
        inv.setValorAplicado(BigDecimal.valueOf(2000));
        inv.setDataAplicacao(LocalDateTime.now().minusDays(10));

        when(clienteDao.buscarPorId(1L)).thenReturn(cliente);
        when(investimentoDao.listaInvestimento(1L)).thenReturn(List.of(inv));

        var resp = facade.executar(1L);
        var dto = (PerfilRiscoResponseDto) resp.getEntity();

        assertEquals(PerfilEnum.CONSERVADOR, dto.perfil());
    }

    @Test
    void deveRetornarPerfilModerado() {
        ClienteEntity cliente = new ClienteEntity();
        cliente.setPreferenciaLiquidez(PreferenciaLiquidezEnum.EQUILIBRIO.getId());

        InvestimentoEntity inv = new InvestimentoEntity();
        inv.setValorAplicado(BigDecimal.valueOf(15000));
        inv.setDataAplicacao(LocalDateTime.now().minusDays(15));

        when(clienteDao.buscarPorId(1L)).thenReturn(cliente);
        when(investimentoDao.listaInvestimento(1L)).thenReturn(List.of(inv));

        var resp = facade.executar(1L);
        var dto = (PerfilRiscoResponseDto) resp.getEntity();

        assertEquals(PerfilEnum.MODERADO, dto.perfil());
    }

    @Test
    void deveRetornarPerfilAgressivo() {
        ClienteEntity cliente = new ClienteEntity();
        cliente.setPreferenciaLiquidez(PreferenciaLiquidezEnum.RENTABILIDADE.getId());

        InvestimentoEntity inv1 = new InvestimentoEntity();
        inv1.setValorAplicado(BigDecimal.valueOf(20000));
        inv1.setDataAplicacao(LocalDateTime.now().minusDays(5));

        InvestimentoEntity inv2 = new InvestimentoEntity();
        inv2.setValorAplicado(BigDecimal.valueOf(20000));
        inv2.setDataAplicacao(LocalDateTime.now().minusDays(3));

        when(clienteDao.buscarPorId(anyLong())).thenReturn(cliente);
        when(investimentoDao.listaInvestimento(anyLong())).thenReturn(List.of(inv1, inv2));

        var resp = facade.executar(1L);
        var dto = (PerfilRiscoResponseDto) resp.getEntity();

        assertEquals(PerfilEnum.AGRESSIVO, dto.perfil());
    }

    @Test
    void deveCalcularPontuacaoCorretamente() {
        ClienteEntity cliente = new ClienteEntity();
        cliente.setPreferenciaLiquidez(PreferenciaLiquidezEnum.RENTABILIDADE.getId());

        InvestimentoEntity inv = new InvestimentoEntity();
        inv.setValorAplicado(BigDecimal.valueOf(20000));
        inv.setDataAplicacao(LocalDateTime.now().minusDays(5));

        when(clienteDao.buscarPorId(1L)).thenReturn(cliente);
        when(investimentoDao.listaInvestimento(1L)).thenReturn(List.of(inv));

        var resp = facade.executar(1L);
        var dto = (PerfilRiscoResponseDto) resp.getEntity();

        assertEquals(80, dto.pontuacao());
        assertEquals(PerfilEnum.AGRESSIVO, dto.perfil());
    }
}

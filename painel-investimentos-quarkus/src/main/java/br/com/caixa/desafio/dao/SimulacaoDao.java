package br.com.caixa.desafio.dao;

import br.com.caixa.desafio.dto.ListaSimulacaoResponseDto;
import br.com.caixa.desafio.dto.SimulacoesProdutoDiaDto;
import br.com.caixa.desafio.entity.SimulacaoEntity;
import br.com.caixa.desafio.exceptions.DatasourceException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@ApplicationScoped
public class SimulacaoDao {

    private static final Logger log = LoggerFactory.getLogger(SimulacaoDao.class);
    @Inject
    EntityManager entityManager;

    @Transactional
    public void salvarSimulacao(SimulacaoEntity simulacao) {
        try{
            entityManager.persist(simulacao);
        } catch (Exception e) {
            throw new DatasourceException("Erro na Persistencia",500,"Falha ao salvar Simulação na Base de dados");
        }
    }

    public List<ListaSimulacaoResponseDto> listarSimulacoes() {
        try{
            StringBuilder queryString = new StringBuilder();
            queryString.append("SELECT SIMULACAO_ID, SIMULACAO_CLIENT_ID, SIMULACAO_DATA_SIMULACAO, SIMULACAO_PRAZO_MESES, SIMULACAO_VALOR_FINAL, SIMULACAO_VALOR_INVESTIDO");
            queryString.append(" ,p.PRODUTO_NOME");
            queryString.append(" FROM SIMULACAO");
            queryString.append(" JOIN PRODUTO AS p on SIMULACAO_PRODUTO_ID = PRODUTO_ID");
            Query query = entityManager.createNativeQuery(queryString.toString());
            List<Object[]> listaObjetos = (List<Object[]>) query.getResultList();

            return listaObjetos.stream().map(this::construirSimulacao).toList();
        } catch (NoResultException e){
            log.error("Lista Vazia: {}", e.getMessage());
            throw new DatasourceException("Lista Vazia", 404, "Nenhuma Simulação encontrada");
        } catch (Exception e) {
            log.error("Erro ao listar simulações: {}", e.getMessage());
            throw new DatasourceException("Erro na Consulta",500,"Falha ao listar Simulações na Base de dados");
        }
    }

    public List<SimulacoesProdutoDiaDto> simulacoesProdutoDia() {
        try{
            StringBuilder queryString = new StringBuilder();
            queryString.append("SELECT p.PRODUTO_NOME, COUNT(s.SIMULACAO_ID) AS TOTAL_SIMULACOES, CAST(s.SIMULACAO_DATA_SIMULACAO AS DATE) AS DATA, AVG(s.SIMULACAO_VALOR_FINAL) AS MEDIA_VALOR_FINAL");
            queryString.append(" FROM SIMULACAO AS s");
            queryString.append(" JOIN PRODUTO AS p on s.SIMULACAO_PRODUTO_ID = p.PRODUTO_ID");
            queryString.append(" GROUP BY p.PRODUTO_NOME, CAST(s.SIMULACAO_DATA_SIMULACAO AS DATE)");
            queryString.append(" ORDER BY DATA DESC, PRODUTO_NOME ASC");
            Query query = entityManager.createNativeQuery(queryString.toString());
            List<Object[]> listaObjetos = (List<Object[]>) query.getResultList();

            return listaObjetos.stream().map(this::construirSimulacaoProdutoDia).toList();
        } catch (NoResultException e){
            log.error("Lista Vazia: {}", e.getMessage());
            throw new DatasourceException("Lista Vazia", 404, "Nenhuma Simulação encontrada");
        } catch (Exception e) {
            log.error("Erro ao listar simulações por produto e dia: {}", e.getMessage());
            throw new DatasourceException("Erro na Consulta",500,"Falha ao listar Simulações por Produto e Dia na Base de dados");
        }
    }

    private SimulacoesProdutoDiaDto construirSimulacaoProdutoDia(Object[] objects) {
        return SimulacoesProdutoDiaDto.builder()
                .produto(objects[0] != null ? objects[0].toString() : null)
                .quantidadeSimulacoes(objects[1] != null ? ((Number) objects[1]).longValue() : null)
                .data(objects[2] != null ? LocalDate.parse(objects[2].toString()) : null)
                .mediaValorFinal(objects[3] != null ? (java.math.BigDecimal) objects[3] : null)
                .build();
    }

    private ListaSimulacaoResponseDto construirSimulacao(Object[] objects) {
        return ListaSimulacaoResponseDto.builder()
                .id(objects[0] != null ? ((Number) objects[0]).longValue() : null)
                .clientId(objects[1] != null ? ((Number) objects[1]).longValue() : null)
                .dataSimulacao(objects[2] != null ? LocalDateTime.parse(objects[2].toString()) : null)
                .prazoMeses(objects[3] != null ? ((Number) objects[3]).intValue() : null)
                .valorFinal(objects[4] != null ? (java.math.BigDecimal) objects[4] : null)
                .valorInvestido(objects[5] != null ? (java.math.BigDecimal) objects[5] : null)
                .produto(objects[6] != null ? objects[6].toString() : null)
                .build();
    }
}

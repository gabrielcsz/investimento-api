package br.com.caixa.desafio.dao;

import br.com.caixa.desafio.dto.InvestimentoResponseDto;
import br.com.caixa.desafio.entity.InvestimentoEntity;
import br.com.caixa.desafio.exceptions.DatasourceException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@ApplicationScoped
public class InvestimentoDao {

    @Inject
    EntityManager entityManager;

    public List<InvestimentoResponseDto> listaInvestimentoCliente(Long clienteId) {
        try{
            StringBuilder queryString = new StringBuilder();
            queryString.append("SELECT INVESTIMENTO_ID, INVESTIMENTO_VALOR_APLICADO, CAST(INVESTIMENTO_DATA_APLICACAO AS DATE), p.PRODUTO_TIPO, p.PRODUTO_RENTABILIDADE");
            queryString.append(" FROM INVESTIMENTO ");
            queryString.append(" JOIN PRODUTO p ON INVESTIMENTO_PRODUTO_ID = p.PRODUTO_ID");
            queryString.append(" WHERE INVESTIMENTO_CLIENTE_ID = :clienteId");

            Query query = entityManager.createNativeQuery(queryString.toString());
            query.setParameter("clienteId", clienteId);

            List<Object[]> listaInvestimentos = query.getResultList();
            return listaInvestimentos.stream().map(this::montarInvestimento).toList();
        } catch (NoResultException e){
            log.error("Lista Vazia para o cliente {}: {}", clienteId, e.getMessage());
            throw new DatasourceException("Lista Vazia", 404, "Nenhum Investimento encontrado para o Cliente");
        } catch (Exception e) {
            log.error("Erro ao buscar investimentos do cliente {}: {}", clienteId, e.getMessage());
            throw new DatasourceException("Erro na Consulta",500, "Falha ao buscar Investimentos na Base de dados");
        }
    }

    private InvestimentoResponseDto montarInvestimento(Object[] objects) {
        return InvestimentoResponseDto.builder()
                .id(((Number) objects[0]).longValue())
                .valor((BigDecimal) objects[1])
                .data(((LocalDate) objects[2]))
                .tipo((String) objects[3])
                .rentabilidade((BigDecimal) objects[4])
                .build();
    }

    public List<InvestimentoEntity> listaInvestimento(Long clienteId) {
        try{
            TypedQuery<InvestimentoEntity> query = entityManager.createQuery("SELECT i FROM InvestimentoEntity i WHERE i.clienteId = :clienteId", InvestimentoEntity.class);
            query.setParameter("clienteId", clienteId);
            return query.getResultList();
        } catch (NoResultException e) {
            log.error("Lista Vazia para o cliente {}: {}", clienteId, e.getMessage());
            throw new DatasourceException("Lista Vazia", 404, "Nenhum Investimento encontrado para o Cliente");
        } catch (Exception e){
            log.error("Erro ao buscar investimentos do cliente {}: {}", clienteId, e.getMessage());
            throw new DatasourceException("Erro na Consulta",500, "Falha ao buscar Investimentos na Base de dados");
        }
    }
}

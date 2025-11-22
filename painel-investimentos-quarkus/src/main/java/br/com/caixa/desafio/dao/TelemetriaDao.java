package br.com.caixa.desafio.dao;

import br.com.caixa.desafio.entity.TelemetriaEntity;
import br.com.caixa.desafio.exceptions.DatasourceException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
@ApplicationScoped
public class TelemetriaDao {

    @Inject
    EntityManager entityManager;

    @Transactional
    public void persistirTelemetria(TelemetriaEntity telemetria){
        try {
            if (telemetria.getId() == null) {
                entityManager.persist(telemetria);
            } else {
                entityManager.merge(telemetria);
            }
        } catch (Exception e) {
            throw new DatasourceException("Erro na Persistencia",500,"Falha ao salvar Telemetria na Base de dados");
        }
    }

    public Optional<TelemetriaEntity> procurarPorServico(String servico) {
       try{
           return entityManager.createQuery(
                           "SELECT t FROM TelemetriaEntity t WHERE t.servico = :servico", TelemetriaEntity.class)
                   .setParameter("servico", servico)
                   .getResultStream()
                   .findFirst();
       } catch (Exception e) {
           throw new DatasourceException("Erro na Consulta",500,"Falha ao buscar Telemetria na Base de dados");
       }
    }

    public List<TelemetriaEntity> listarTelemetrias() {
        try {
            TypedQuery<TelemetriaEntity> telemetria = entityManager.createQuery("SELECT p FROM TelemetriaEntity p", TelemetriaEntity.class);
            return telemetria.getResultList();
        } catch (Exception e) {
            throw new DatasourceException("Erro na Consulta", 500, "Falha ao listar Telemetrias na Base de dados");
        }
    }
}

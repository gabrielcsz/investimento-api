package br.com.caixa.desafio.dao;

import br.com.caixa.desafio.entity.ClienteEntity;
import br.com.caixa.desafio.exceptions.DatasourceException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class ClienteDao {

    @Inject
    EntityManager entityManager;

    public ClienteEntity buscarPorId(Long clienteId) {
        try {
            return entityManager.find(ClienteEntity.class, clienteId);
        } catch (NoResultException e){
            log.error("Cliente com ID {} não encontrado: {}", clienteId, e.getMessage());
            throw new DatasourceException("Cliente nao encontrado", 404, "ID de Cliente inválido");
        } catch (Exception e) {
            log.error("Erro ao buscar cliente {}: {}", clienteId, e.getMessage());
            throw new DatasourceException("Erro na Consulta",500,"Falha ao buscar Cliente na Base de dados");
        }
    }

}

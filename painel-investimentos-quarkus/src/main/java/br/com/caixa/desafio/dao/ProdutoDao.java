package br.com.caixa.desafio.dao;

import br.com.caixa.desafio.dto.ProdutoValidadoDto;
import br.com.caixa.desafio.entity.ProdutoEntity;
import br.com.caixa.desafio.exceptions.DatasourceException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@ApplicationScoped
public class ProdutoDao {

    private static final Logger log = LoggerFactory.getLogger(ProdutoDao.class);
    @Inject
    EntityManager entityManager;

    public ProdutoValidadoDto buscarProdutoPorTipo(String tipo) {
        try{
            StringBuilder queryString = new StringBuilder();
            queryString.append("SELECT PRODUTO_ID, PRODUTO_NOME, PRODUTO_TIPO, PRODUTO_RENTABILIDADE, PRODUTO_RISCO ");
            queryString.append("FROM dbo.PRODUTO ");
            queryString.append("WHERE PRODUTO_TIPO = :tipo ");

            Query query = entityManager.createNativeQuery(queryString.toString());
            query.setParameter("tipo", tipo);
            Object[] objects = (Object[]) query.getSingleResult();
            ProdutoValidadoDto produto = construirProduto(objects);
            return produto;
        } catch (NoResultException e){
            log.error("Produto do tipo {} não encontrado: {}", tipo, e.getMessage());
            throw new DatasourceException("Produto nao encontrado", 404, "Tipo de produto inválido");
        } catch (Exception e) {
            log.error("Erro ao buscar produto do tipo {}: {}", tipo, e.getMessage());
            throw new DatasourceException("Erro na Consulta", 500, "Falha ao buscar tipo de Produto na Base de dados");
        }
    }

    private ProdutoValidadoDto construirProduto(Object[] objects) {
        return ProdutoValidadoDto.builder()
                .id(objects[0] != null ? Long.parseLong(objects[0].toString()) : null)
                .nome(objects[1] != null ? objects[1].toString() : null)
                .tipo(objects[2] != null ? objects[2].toString() : null)
                .rentabilidade(objects[3] != null ?  new java.math.BigDecimal(objects[3].toString()) : null)
                .risco(objects[4] != null ? objects[4].toString() : null)
                .build();
    }

    public List<ProdutoEntity> listarTodosProdutos() {
        try{
            TypedQuery<ProdutoEntity> produtos = entityManager.createQuery("SELECT p FROM ProdutoEntity p", ProdutoEntity.class);
            return produtos.getResultList();
        } catch (NoResultException e) {
            log.error("Lista Vazia de Produtos: {}", e.getMessage());
            throw new DatasourceException("Lista Vazia", 404, "Nenhum Produto encontrado");
        } catch (Exception e) {
            throw new DatasourceException("Erro na Consulta",500,"Falha ao listar Produtos na Base de dados");
        }
    }
}

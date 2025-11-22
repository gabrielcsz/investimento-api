📌 Painel de Investimentos – API (Quarkus)

Este projeto implementa uma API em Quarkus para um Painel de Investimentos, incluindo cadastro de produtos, simulações, cálculo de rentabilidade e integração com Keycloak para autenticação.
O projeto está preparado para rodar localmente e via Docker/Docker Compose.

🚀 Tecnologias utilizadas
Backend

 - Quarkus (Java 21)

 - Hibernate ORM

 - Keycloak (OIDC)

 - Maven

Para rodar o Projeto, precisa ter o JDK 21 e o Maven instalados na sua máquina.
Para rodar localmente, rode o DockerCompose
```bash
docker-compose up -d
```

Em seguida, para rodar a aplicação Quarkus localmente, use o comando:
```bash
./mvnw quarkus:dev
```
ou 
```bash
mvn quarkus:dev
```

A aplicação estará disponível em `http://localhost:8080`.
pode abrir o Swagger UI em `http://localhost:8080/q/swagger-ui/` para explorar os endpoints da API.

***Endpoints principais

Produtos

 - GET /produtos-recomendados/{perfil}

Simulações

 - POST /simulacoes/simular-investimento

 - GET /simulacoes

 - GET /simulacoes/por-produto-dia

Perfil de risco

 - GET /perfil-risco/{clienteId}

Investimentos

 - GET /investimentos/{idCliente}

Telemetria

 - GET /telemetria

Para Acesar os Endpoints será necessário um Bearer token JWT válido do Keycloak. 

POSTMAN Collection
 - Uma coleção Postman está disponível para facilitar os testes dos endpoints da API. Você pode importar o arquivo `InvestimentosAPI.postman_collection.json` no Postman para começar a usar.


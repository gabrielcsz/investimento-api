package br.com.caixa.desafio.integracao;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class CriarSimulacaoIntegracaoTest {

    @Test
    public void testCriarSimulacaoComToken() {

        String jsonSimulacao = """
        {
            "clienteId": 123,
            "valor": 10000.00,
            "tipoProduto": "CDB",
            "prazoMeses": 6
        }
        """;
        
        String token = gerarTokenClientCredentials();

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(jsonSimulacao)
                .when()
                .post("/simulacoes/simular-investimento")
                .then()
                .statusCode(201);

    }

    private String gerarTokenClientCredentials() {
        return given()
                .relaxedHTTPSValidation()
                .contentType("application/x-www-form-urlencoded")
                .formParam("client_id", "investimento-api")
                .formParam("client_secret", "mANuNvtsEcgzqPQ0zKFumoNPwWvrRS9B")
                .formParam("grant_type", "client_credentials")
                .post("http://localhost:8081/realms/investimento-realm/protocol/openid-connect/token")
                .then()
                .statusCode(200)
                .extract()
                .path("access_token");
    }
}

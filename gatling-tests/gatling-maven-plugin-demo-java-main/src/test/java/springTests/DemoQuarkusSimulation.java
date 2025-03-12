package springTests;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class DemoQuarkusSimulation extends Simulation {

    // Definindo o protocolo HTTP
    HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://quarkus.local") // URL da sua API Spring Boot
            .acceptHeader("application/json")
            .contentTypeHeader("application/json");

    // Definindo o cenário de teste
    ScenarioBuilder scn = scenario("DemoSpringAPI Test")
            // Criar uma nova entidade
            .exec(http("Create Entity")
                    .post("/demo")
                    .body(StringBody("{\"name\": \"Sample Entity\", \"description\": \"This is a test entity\"}")).asJson()
                    .check(status().is(201))
                    .check(jsonPath("$.id").saveAs("entityId")))  // Salvando o ID da entidade criada

            .exec(http("Get Entity By ID")
                    .get("/demo/#{entityId}")  // Usando o ID salvo na requisição GET
                    .check(status().is(200)))

            // Obter todas as entidades
            .exec(http("Get All Entities")
                    .get("/demo")
                    .check(status().is(200)))
            .pause(1)  // Pause entre as requisições

            // Atualizar uma entidade
            .exec(http("Update Entity")
                    .put("/demo/#{entityId}") // Aqui a ID é estática apenas como exemplo, mas você pode tornar dinâmico
                    .body(StringBody("{\"name\": \"Updated Entity\", \"description\": \"Updated description\"}")).asJson()
                    .check(status().is(200)))
            .pause(1)  // Pause entre as requisições

            // Deletar a entidade
            .exec(http("Delete Entity")
                    .delete("/demo/#{entityId}")  // ID de novo sendo estática, pode ser dinâmica se necessário
                    .check(status().is(200)));

    // Configuração do teste com um único PopulationBuilder
    {
        setUp(
                scn.injectOpen(atOnceUsers(20)) // 10 usuários simultâneos
        ).protocols(httpProtocol); // Definindo o protocolo HTTP
    }
}
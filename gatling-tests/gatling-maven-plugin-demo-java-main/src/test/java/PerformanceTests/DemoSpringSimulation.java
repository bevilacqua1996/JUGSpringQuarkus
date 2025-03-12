package PerformanceTests;

import io.gatling.javaapi.core.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class DemoSpringSimulation extends Simulation {

    private static final String BASE_URL = "http://spring.local/api/demo";

    // Scenario: Simulação de requisições para a API
    private ScenarioBuilder scn = scenario("Spring API Test")
            .exec(http("Create Entity")
                    .post(BASE_URL)
                    .header("Content-Type", "application/json")
                    .body(StringBody("{\"name\": \"Sample Entity\"}"))
                    .check(status().is(201))
                    .check(jsonPath("$.id").saveAs("entityId"))
            )
            .pause(2)
            .exec(http("Get All Entities")
                    .get(BASE_URL)
                    .check(status().is(200))
            )
            .pause(2)
            .exec(http("Get Entity By ID")
                    .get(BASE_URL + "/#{entityId}")
                    .check(status().is(200))
            )
            .pause(2)
            .exec(http("Update Entity")
                    .put(BASE_URL + "/#{entityId}")
                    .header("Content-Type", "application/json")
                    .body(StringBody("{\"name\": \"Updated Entity\"}"))
                    .check(status().is(200))
            )
            .pause(2)
            .exec(http("Delete Entity")
                    .delete(BASE_URL + "/#{entityId}")
                    .check(status().is(204))
            );

    // Configuração da simulação
    {
        setUp(
                scn.injectOpen(rampUsers(10).during(10)) // 10 usuários em 10 segundos
        ).protocols(http.baseUrl("http://spring.local"));
    }
}
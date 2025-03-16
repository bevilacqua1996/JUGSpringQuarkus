package PerformanceTests;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class DemoQuarkusSimulation extends Simulation {

    private static final String BASE_URL = "http://localhost:8080/demo";

    // Scenario: API requirements
    private ScenarioBuilder scn = scenario("Quarkus API Test")
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

    // Real behaviour with ramp up and ramp down
    {
        setUp(
                scn.injectOpen(
                        rampUsers(25).during(120),  // Increases the load
                        constantUsersPerSec(50).during(120),  // Keep it stable
                        rampUsersPerSec(50).to(10).during(60)  // Decreases the load
                )
        ).protocols(http.baseUrl("http://localhost:8080"));
    }
}
package PerformanceTests;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class DemoQuarkusSimulation extends Simulation {

    private static final String BASE_URL = "http://localhost:8082";
    private static final String BASE_URL_API = "http://localhost:8082/demo";

    private static final int RAMP_USERS=5;
    private static final int CONSTANT_USERS=10;
    private static final int RAMP_DOWN_USERS=5;

    private static final int RAMP_TIME_SEC=30;
    private static final int CONSTANT_TIME_SEC=60;
    private static final int RAMP_DOWN_TIME_SEC=30;

    // Scenario: API requirements
    private ScenarioBuilder scn = scenario("Quarkus API Test")
            .exec(http("Create Entity")
                    .post(BASE_URL_API)
                    .header("Content-Type", "application/json")
                    .body(StringBody("{\"name\": \"Sample Entity\"}"))
                    .check(status().is(201))
                    .check(jsonPath("$.id").saveAs("entityId"))
            )
            .pause(2)
            .exec(http("Get All Entities")
                    .get(BASE_URL_API)
                    .check(status().is(200))
            )
            .pause(2)
            .exec(http("Get Entity By ID")
                    .get(BASE_URL_API + "/#{entityId}")
                    .check(status().is(200))
            )
            .pause(2)
            .exec(http("Update Entity")
                    .put(BASE_URL_API + "/#{entityId}")
                    .header("Content-Type", "application/json")
                    .body(StringBody("{\"name\": \"Updated Entity\"}"))
                    .check(status().is(200))
            )
            .pause(2)
            .exec(http("Delete Entity")
                    .delete(BASE_URL_API + "/#{entityId}")
                    .check(status().is(204))
            );

    // Real behaviour with ramp up and ramp down
    {
        setUp(
                scn.injectOpen(
                        rampUsers(RAMP_USERS).during(RAMP_TIME_SEC),  // Increases the load
                        constantUsersPerSec(CONSTANT_USERS).during(CONSTANT_TIME_SEC),  // Keep it stable
                        rampUsersPerSec(CONSTANT_USERS).to(RAMP_DOWN_USERS).during(RAMP_DOWN_TIME_SEC)  // Decreases the load
                )
        ).protocols(http.baseUrl(BASE_URL));
    }
}
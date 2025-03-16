package PerformanceTests;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class MultiAppSimulation extends Simulation {

    private static final String BASE_URL_1 = "http://localhost:8080/demo";
    private static final String BASE_URL_2 = "http://localhost:8081/api/demo";
    private static final String BASE_URL_3 = "http://localhost:8082/demo";
    private static final String BASE_URL_4 = "http://localhost:8083/api/demo";

    private static final int RAMP_USERS=10;
    private static final int CONSTANT_USERS=15;
    private static final int RAMP_DOWN_USERS=5;

    private static final int RAMP_TIME_SEC=45;
    private static final int CONSTANT_TIME_SEC=60;
    private static final int RAMP_DOWN_TIME_SEC=60;

    // Scenario 1: API requirements for App 1
    private ScenarioBuilder scnApp1 = scenario("App 1 - Quarkus")
            .exec(http("Create Entity - App 1")
                    .post(BASE_URL_1)
                    .header("Content-Type", "application/json")
                    .body(StringBody("{\"name\": \"Sample Entity\"}"))
                    .check(status().is(201))
                    .check(jsonPath("$.id").saveAs("entityId"))
            )
            .pause(2)
            .exec(http("Get All Entities - App 1")
                    .get(BASE_URL_1)
                    .check(status().is(200))
            )
            .pause(2)
            .exec(http("Get Entity By ID - App 1")
                    .get(BASE_URL_1 + "/#{entityId}")
                    .check(status().is(200))
            );

    // Scenario 2: API requirements for App 2
    private ScenarioBuilder scnApp2 = scenario("App 2 - Spring")
            .exec(http("Create Entity - App 2")
                    .post(BASE_URL_2)
                    .header("Content-Type", "application/json")
                    .body(StringBody("{\"name\": \"Sample Entity\"}"))
                    .check(status().is(201))
                    .check(jsonPath("$.id").saveAs("entityId"))
            )
            .pause(2)
            .exec(http("Get All Entities - App 2")
                    .get(BASE_URL_2)
                    .check(status().is(200))
            )
            .pause(2)
            .exec(http("Get Entity By ID - App 2")
                    .get(BASE_URL_2 + "/#{entityId}")
                    .check(status().is(200))
            );

    // Scenario 3: API requirements for App 3
    private ScenarioBuilder scnApp3 = scenario("App 3 - Quarkus Native")
            .exec(http("Create Entity - App 3")
                    .post(BASE_URL_3)
                    .header("Content-Type", "application/json")
                    .body(StringBody("{\"name\": \"Sample Entity\"}"))
                    .check(status().is(201))
                    .check(jsonPath("$.id").saveAs("entityId"))
            )
            .pause(2)
            .exec(http("Get All Entities - App 3")
                    .get(BASE_URL_3)
                    .check(status().is(200))
            )
            .pause(2)
            .exec(http("Get Entity By ID - App 3")
                    .get(BASE_URL_3 + "/#{entityId}")
                    .check(status().is(200))
            );

    // Scenario 4: API requirements for App 4
    private ScenarioBuilder scnApp4 = scenario("App 4 - Spring Native")
            .exec(http("Create Entity - App 4")
                    .post(BASE_URL_4)
                    .header("Content-Type", "application/json")
                    .body(StringBody("{\"name\": \"Sample Entity\"}"))
                    .check(status().is(201))
                    .check(jsonPath("$.id").saveAs("entityId"))
            )
            .pause(2)
            .exec(http("Get All Entities - App 4")
                    .get(BASE_URL_4)
                    .check(status().is(200))
            )
            .pause(2)
            .exec(http("Get Entity By ID - App 4")
                    .get(BASE_URL_4 + "/#{entityId}")
                    .check(status().is(200))
            );

    // Real behaviour with ramp up and ramp down
    {
        setUp(
                scnApp1.injectOpen(
                    rampUsers(RAMP_USERS).during(RAMP_TIME_SEC),  
                    constantUsersPerSec(CONSTANT_USERS).during(CONSTANT_TIME_SEC),  
                    rampUsersPerSec(CONSTANT_USERS).to(RAMP_DOWN_USERS).during(RAMP_DOWN_TIME_SEC)  
                ).protocols(http.baseUrl(BASE_URL_1)), // Protocolo para o primeiro endpoint
                scnApp2.injectOpen(
                    rampUsers(RAMP_USERS).during(RAMP_TIME_SEC),  
                    constantUsersPerSec(CONSTANT_USERS).during(CONSTANT_TIME_SEC),  
                    rampUsersPerSec(CONSTANT_USERS).to(RAMP_DOWN_USERS).during(RAMP_DOWN_TIME_SEC)  
                ).protocols(http.baseUrl(BASE_URL_2)), // Protocolo para o segundo endpoint
                scnApp3.injectOpen(
                    rampUsers(RAMP_USERS).during(RAMP_TIME_SEC),  
                    constantUsersPerSec(CONSTANT_USERS).during(CONSTANT_TIME_SEC),  
                    rampUsersPerSec(CONSTANT_USERS).to(RAMP_DOWN_USERS).during(RAMP_DOWN_TIME_SEC)  
                ).protocols(http.baseUrl(BASE_URL_3)), // Protocolo para o terceiro endpoint
                scnApp4.injectOpen(
                    rampUsers(RAMP_USERS).during(RAMP_TIME_SEC),  
                    constantUsersPerSec(CONSTANT_USERS).during(CONSTANT_TIME_SEC),  
                    rampUsersPerSec(CONSTANT_USERS).to(RAMP_DOWN_USERS).during(RAMP_DOWN_TIME_SEC)  
                ).protocols(http.baseUrl(BASE_URL_4))  // Protocolo para o quarto endpoint
        );
    }
}

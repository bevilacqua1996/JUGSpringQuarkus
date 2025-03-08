package com.jug.demo;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import com.jug.demo.entities.DemoEntity;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.common.http.TestHTTPResource;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.Test;

import java.net.URL;

@QuarkusTest
public class DemoResourceTest {

    @TestHTTPResource("/demo")
    URL demoResourceUrl;

    @Test
    public void testGetAllEmpty() {
        given()
                .when().get(demoResourceUrl)
                .then()
                .statusCode(200)
                .body(is("[]"));
    }

    @Test
    public void testCreateAndGetById() {
        // Create a new DemoEntity
        DemoEntity demo = new DemoEntity();
        demo.setName("Test Name");

        // POST: Create new entity
        String createdId = given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(demo)
                .when().post(demoResourceUrl)
                .then()
                .statusCode(201)
                .body("id", notNullValue()) // Assert that the id is generated
                .extract().path("id").toString();

        // GET: Fetch by ID
        given()
                .when().get(demoResourceUrl + "/" + createdId)
                .then()
                .statusCode(200)
                .body("name", is("Test Name"));
    }

    @Test
    public void testUpdate() {
        // Create a new entity
        DemoEntity demo = new DemoEntity();
        demo.setName("Initial Name");

        String createdId = given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(demo)
                .when().post(demoResourceUrl)
                .then()
                .statusCode(201)
                .extract().path("id").toString();

        // Update the entity
        demo.setName("Updated Name");

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(demo)
                .when().put(demoResourceUrl + "/" + createdId)
                .then()
                .statusCode(200)
                .body("name", is("Updated Name"));
    }

    @Test
    public void testDelete() {
        // Create a new entity
        DemoEntity demo = new DemoEntity();
        demo.setName("To Be Deleted");

        String createdId = given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(demo)
                .when().post(demoResourceUrl)
                .then()
                .statusCode(201)
                .extract().path("id").toString();

        // Delete the entity
        given()
                .when().delete(demoResourceUrl + "/" + createdId)
                .then()
                .statusCode(204);

        // Verify the entity is deleted
        given()
                .when().get(demoResourceUrl + "/" + createdId)
                .then()
                .statusCode(404);
    }
}
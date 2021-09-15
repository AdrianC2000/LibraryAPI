package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class ExampleResourceTest {

    @Test
    public void testUsersEndpoint() {
        given()
                .when().get("/users")
                .then()
                .statusCode(200);
    }

}
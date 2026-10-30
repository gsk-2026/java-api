package com.dreamtech.practicesoftwaretestingapi.tests.user;

import com.dreamtech.practicesoftwaretestingapi.model.UserGetResponse;
import com.dreamtech.practicesoftwaretestingapi.tests.base.UserBaseTest;
import com.dreamtech.practicesoftwaretestingapi.utils.TestStatusLogger;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;


@ExtendWith(TestStatusLogger.class)
public class UserGetByIdTest extends UserBaseTest {

    @Test
    @DisplayName("User : Get user by Id : Success 200")
    public void testUserGetByIdSuccess200() {
        Response getResponse = given()
                .header("Authorization", "Bearer " + bearerToken)
                .pathParam("userId", userId)
                .when()
                .get("/users/{userId}");

        getResponse.then()
                .log().all()
                .statusCode(200)
                .body("id", equalTo(userId))
                .body("first_name", notNullValue())
                .body("last_name", notNullValue())
                .body("dob", notNullValue())
                .body("address", notNullValue())
                .body("city", notNullValue())
                .body("country", notNullValue())
                .body("phone", notNullValue())
                .body("email", notNullValue())
                .body("created_at", notNullValue())
                .extract()
                .as(UserGetResponse.class);

        userId = getResponse.jsonPath().getInt("id");
    }

    @Test
    @DisplayName("User : Get user by Id : Unauthorized 401 - Invalid Token")
    public void testUserGetByIdUnauthorized401_InvalidToken() {
        given()
                .header("Authorization", "Bearer ABCDEFGHIJKLMNOPQRSTUVWXYZ")
                .pathParam("userId", userId)
                .when()
                .get("/users/{userId}")
                .then()
                .log().all()
                .statusCode(401)
                .body("message", equalTo("Unauthorized"));
    }

    @Test
    @DisplayName("User : Get user by Id : Not Found 404 - Invalid ID")
    public void testUserGetByNotFound404_InvalidId() {
        given()
                .header("Authorization", "Bearer " + bearerToken)
                .pathParam("userId", 123456)
                .when()
                .get("/users/{userId}")
                .then()
                .log().all()
                .statusCode(404)
                .body("message", equalTo("Requested item not found"));
    }

    @Test
    @DisplayName("User : Get user by Id : Not Found 404 - Invalid Endpoint")
    public void testUserGetByNotFound404_InvalidEndpoint() {
        Response getResponse = given()
                .header("Authorization", "Bearer " + bearerToken)
                .pathParam("userId", userId)
                .when()
                .get("/userid/{userId}");

        getResponse.then()
                .log().all()
                .statusCode(404)
                .body("message", equalTo("Resource not found"));
    }

    @Test
    @DisplayName("User : Get user by Id : Method Not Allowed 405 - Invalid Method PATCH")
    public void testUserGetByMethodNotAllowed405_InvalidEndpoint() {
        Response getResponse = given()
                .header("accept", "application/json")
                .header("Authorization", "Bearer " + bearerToken)
                .pathParam("userId", userId)
                .when()
                .patch("/users/{userId}");

        getResponse.then()
                .log().all()
                .statusCode(405)
                .body("message", equalTo("Method is not allowed for the requested route"));
    }
}

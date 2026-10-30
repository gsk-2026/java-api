package com.dreamtech.practicesoftwaretestingapi.tests.user;

import com.dreamtech.practicesoftwaretestingapi.model.UserRefreshResponse;
import com.dreamtech.practicesoftwaretestingapi.tests.base.UserBaseTest;
import com.dreamtech.practicesoftwaretestingapi.utils.TestStatusLogger;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThan;


@ExtendWith(TestStatusLogger.class)
public class UserGetRefreshTest extends UserBaseTest {

    @Test
    @DisplayName("User : Get refresh : Success 200")
    public void testUserGetRefreshSuccess200() {
        Response refreshResponse = given()
                .header("Authorization", "Bearer " + bearerToken)
                .when()
                .get("/users/refresh");

        refreshResponse.then()
                .log().all()
                .statusCode(200)
                .body("access_token", notNullValue())
                .body("token_type", equalTo("bearer"))
                .body("expires_in", greaterThan(0))
                .extract()
                .as(UserRefreshResponse.class);

        bearerToken = refreshResponse.jsonPath().getString("access_token");
    }

    @Test
    @DisplayName("User : Get refresh : Unauthorized 401 - Invalid Token & Endpoint")
    public void testUserGetRefreshUnauthorized401_InvalidTokenEndpoint() {
        given()
                .header("Authorization", "Bearer ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789")
                .when()
                .get("/users/reload")
                .then()
                .log().all()
                .statusCode(401)
                .body("message", equalTo("Unauthorized"));
    }

    @Test
    @DisplayName("User : Get refresh : Not Found 404 - Invalid Endpoint")
    public void testUserGetRefreshNotFound404_InvalidEndpoint() {
        given()
                .header("Authorization", "Bearer " + bearerToken)
                .when()
                .get("/users/reload")
                .then()
                .log().all()
                .statusCode(404)
                .body("message", equalTo("Requested item not found"));
    }

    @Test
    @DisplayName("User : Get refresh : Internal Server Error 500 - Invalid Token")
    public void testUserGetRefreshInternalServerError500_InvalidToken() {
        given()
                .header("Authorization", "Bearer ABCDEFGHIJKLMNOPQSTUVWXUZ0123456789")
                .when()
                .get("/users/refresh")
                .then()
                .log().all()
                .statusCode(500)
                .body("message", equalTo("Server Error"));
    }

}

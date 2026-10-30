package com.dreamtech.practicesoftwaretestingapi.tests.user;

import com.dreamtech.practicesoftwaretestingapi.model.UserLoginRequest;
import com.dreamtech.practicesoftwaretestingapi.model.UserLoginResponse;
import com.dreamtech.practicesoftwaretestingapi.model.UserLogoutResponse;
import com.dreamtech.practicesoftwaretestingapi.tests.base.UserBaseTest;
import com.dreamtech.practicesoftwaretestingapi.utils.TestStatusLogger;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;


@ExtendWith(TestStatusLogger.class)
public class UserLogoutTest extends UserBaseTest {

    @Test
    @DisplayName("User : Logout : Success 200")
    public void testUserLogoutSuccess200() {
        given()
                .header("Authorization", "Bearer " + bearerToken)
                .when()
                .get("/users/logout")
                .then()
                .log().all()
                .statusCode(200)
                .body("message", equalTo("Successfully logged out"))
                .extract()
                .as(UserLogoutResponse.class);
    }

    @Test
    @DisplayName("User : Logout : Unauthorized 401 - Invalid Token")
    public void testUserLogoutUnauthorized401_InvalidToken() {
        given()
                .header("Authorization", "Bearer ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789")
                .when()
                .get("/users/logout")
                .then()
                .log().all()
                .statusCode(401)
                .body("message", equalTo("Unauthorized"));
    }

    @Test
    @DisplayName("User : Logout : Not Found 404 - Invalid Endpoint")
    public void testUserLogoutNotFound404() {
        // Login user
        UserLoginRequest requestBody = UserLoginRequest.builder()
                .email("customer@practicesoftwaretesting.com")
                .password("welcome01")
                .build();

        Response loginResponse = given()
                .body(requestBody)
                .when()
                .post("/users/login");

        loginResponse.then()
                .log().all()
                .statusCode(200)
                .body("access_token", notNullValue())
                .body("token_type", notNullValue())
                .body("expires_in", greaterThan(0))
                .extract()
                .as(UserLoginResponse.class);

        bearerToken = loginResponse.jsonPath().getString("access_token");

        // Logout user with invalid endpoint
        given()
                .header("Authorization", "Bearer " + bearerToken)
                .when()
                .get("/users/byebye")
                .then()
                .log().all()
                .statusCode(404)
                .body("message", equalTo("Requested item not found"));
    }

}

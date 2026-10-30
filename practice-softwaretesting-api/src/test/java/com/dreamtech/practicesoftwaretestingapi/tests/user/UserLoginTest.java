package com.dreamtech.practicesoftwaretestingapi.tests.user;

import com.dreamtech.practicesoftwaretestingapi.model.UserLoginRequest;
import com.dreamtech.practicesoftwaretestingapi.model.UserLoginResponse;
import com.dreamtech.practicesoftwaretestingapi.tests.base.UserBaseTest;
import com.dreamtech.practicesoftwaretestingapi.utils.TestStatusLogger;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


@ExtendWith(TestStatusLogger.class)
public class UserLoginTest extends UserBaseTest {

    @Test
    @DisplayName("User : Login user : Success 200")
    public void testUserLoginSuccess() {
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
    }

    @Test
    @DisplayName("User : Login user : Unauthorized 401 - Invalid Email")
    public void testUserLoginUnauthorized401_InvalidEmail() {

        UserLoginRequest requestBody = UserLoginRequest.builder()
                .email("customerpracticesoftwaretesting.com")
                .password("welcome01")
                .build();

        Response loginResponse = given()
                .body(requestBody)
                .when()
                .post("/users/login");

        loginResponse.then()
                .log().all()
                .statusCode(401)
                .body("error", equalTo("Unauthorized"));
    }

    @Test
    @DisplayName("User : Login user : Unauthorized 401 - Invalid Password")
    public void testUserLoginUnauthorized401_InvalidPswd() {
        UserLoginRequest requestBody = UserLoginRequest.builder()
                .email("customer@practicesoftwaretesting.com")
                .password("welcome0123")
                .build();

        Response loginResponse = given()
                .body(requestBody)
                .when()
                .post("/users/login");

        loginResponse.then()
                .log().all()
                .statusCode(401)
                .body("error", equalTo("Unauthorized"));
    }

    @Test
    @DisplayName("User : Login user : Unauthorized 401 - Invalid Request")
    public void testUserLoginUnauthorized422_InvalidPswd() {
        UserLoginRequest requestBody = UserLoginRequest.builder().build();

        Response loginResponse = given()
                .body(requestBody)
                .when()
                .post("/users/login");

        loginResponse.then()
                .log().all()
                .statusCode(401)
                .body("error", equalTo("Unauthorized"));
    }

}

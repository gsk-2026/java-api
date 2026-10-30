package com.dreamtech.practicesoftwaretestingapi.tests.base;

import com.dreamtech.practicesoftwaretestingapi.model.UserLoginRequest;
import com.dreamtech.practicesoftwaretestingapi.model.UserRegisterRequest;
import com.dreamtech.practicesoftwaretestingapi.model.UserRegisterResponse;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


public class UserBaseTest extends ApiBaseTest {

    protected static String bearerToken;
    protected static Integer userId;

    @BeforeAll
    public static void setup() {
        // User registration
        String userEmail = "test.user." + UUID.randomUUID().toString().substring(0, 8) + "@toolshop.com";
        String userPassword = "Welcome123!";

        UserRegisterRequest registerRequestBody = UserRegisterRequest.builder()
                .first_name("Jane")
                .last_name("Doe")
                .dob("2008-05-15")
                .address("456 Test Ave")
                .city("Chicago")
                .state("IL")
                .country("US")
                .postcode("67890")
                .phone("888-123-4567")
                .email(userEmail)
                .password(userPassword)
                .build();

        Response registerResponse = given()
                .contentType(ContentType.JSON)
                .body(registerRequestBody)
                .when()
                .post("/users/register");

        registerResponse.then()
                //.log().all()
                .statusCode(201)
                .body("first_name", equalTo(registerRequestBody.first_name()))
                .body("last_name", equalTo(registerRequestBody.last_name()))
                .body("dob", equalTo(registerRequestBody.dob()))
                .body("address", equalTo(registerRequestBody.address()))
                .body("city", equalTo(registerRequestBody.city()))
                .body("state", equalTo(registerRequestBody.state()))
                .body("country", equalTo(registerRequestBody.country()))
                .body("postcode", equalTo(registerRequestBody.postcode()))
                .body("phone", equalTo(registerRequestBody.phone()))
                .body("email", equalTo(registerRequestBody.email()))
                .body("password", nullValue())
                .body("created_at", notNullValue())
                .body("id", greaterThan(0))
                .extract()
                .as(UserRegisterResponse.class);

        userId = registerResponse.jsonPath().getInt("id");

        // User login
        UserLoginRequest loginRequestBody = UserLoginRequest.builder()
                .email(userEmail)
                .password(userPassword)
                .build();

        Response loginResponse = given()
                .contentType(ContentType.JSON)
                .body(loginRequestBody)
                .when()
                .post("/users/login");

        loginResponse.then()
                //.log().all()
                .statusCode(200)
                .body("access_token", notNullValue())
                .body("token_type", notNullValue())
                .body("expires_in", greaterThan(0));

        bearerToken = loginResponse.jsonPath().getString("access_token");

        // User authorization
        given()
                .accept(ContentType.JSON)
                .header("Authorization", "Bearer " + bearerToken)
                .when()
                .get("/users/me")
                .then()
                //.log().all()
                .statusCode(200)
                .body("first_name", equalTo(registerRequestBody.first_name()))
                .body("last_name", equalTo(registerRequestBody.last_name()))
                .body("dob", equalTo(registerRequestBody.dob()))
                .body("address", equalTo(registerRequestBody.address()))
                .body("city", equalTo(registerRequestBody.city()))
                .body("state", equalTo(registerRequestBody.state()))
                .body("country", equalTo(registerRequestBody.country()))
                .body("postcode", equalTo(registerRequestBody.postcode()))
                .body("phone", equalTo(registerRequestBody.phone()))
                .body("email", equalTo(registerRequestBody.email()))
                .body("password", nullValue())
                .body("created_at", notNullValue())
                .body("id", greaterThan(0))
                .extract()
                .as(UserRegisterResponse.class);
    }

}


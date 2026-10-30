package com.dreamtech.practicesoftwaretestingapi.tests.user;

import com.dreamtech.practicesoftwaretestingapi.model.UserGetResponse;
import com.dreamtech.practicesoftwaretestingapi.tests.base.UserBaseTest;
import com.dreamtech.practicesoftwaretestingapi.utils.TestStatusLogger;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;


@ExtendWith(TestStatusLogger.class)
public class UserGetMeTest extends UserBaseTest {

    @Test
    @DisplayName("User : Get user by me : Success 200")
    public void testUserGetMeSuccess200() {
        Response meResponse = given()
                .header("Authorization", "Bearer " + bearerToken)
                .when()
                .get("/users/me");

        meResponse.then()
                .log().all()
                .statusCode(200)
                .body("id", notNullValue())
                .body("first_name", notNullValue())
                .body("last_name", notNullValue())
                .body("dob", notNullValue())
                .body("address", notNullValue())
                .body("city", notNullValue())
                //.body("state", notNullValue())
                //.body("postcode", notNullValue())
                .body("country", notNullValue())
                .body("phone", notNullValue())
                .body("email", notNullValue())
                .body("created_at", notNullValue())
                .extract()
                .as(UserGetResponse.class);

        userId = meResponse.jsonPath().getInt("id");
    }

    @Test
    @DisplayName("User : Get user by me : Unauthorized 401 - Invalid Token")
    public void testUserGetMeUnauthorized401_InvalidToken() {
        Response meResponse = given()
                .header("Authorization", "Bearer 0123456789ABCDEFGHIJKLMNOPQSTUVWXUZ")
                .when()
                .get("/users/me");

        meResponse.then()
                .log().all()
                .statusCode(401)
                .body("message", equalTo("Unauthorized"));
    }

    @Test
    @DisplayName("User : Get user by me : Not Found 404 - Invalid Endpoint")
    public void testUserGetMeNotFound404_InvalidEndpoint() {
        Response meResponse = given()
                .header("Authorization", "Bearer " + bearerToken)
                .when()
                .get("/users/youme");

        meResponse.then()
                .log().all()
                .statusCode(404)
                .body("message", equalTo("Requested item not found"));
    }
}

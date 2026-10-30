package com.dreamtech.practicesoftwaretestingapi.tests.user;

import com.dreamtech.practicesoftwaretestingapi.tests.base.UserBaseTest;
import com.dreamtech.practicesoftwaretestingapi.utils.TestStatusLogger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;


@ExtendWith(TestStatusLogger.class)
public class UserGetAllTest extends UserBaseTest {

    @Test
    @DisplayName("User : Get all users : Success 200")
    public void testUserGetAllUsersSuccess200() {
        int pageNumber = new Random().nextInt(10) - 5;

        given()
                .header("Authorization", "Bearer " + bearerToken)
                .pathParam("pageNumber", pageNumber)
                .when()
                .get("/users?page={pageNumber}")
                .then()
                .log().all()
                .statusCode(200)
                .body("current_page", equalTo((pageNumber > 0) ? pageNumber : 1))
                //.body("from", notNullValue())
                //.body("to", notNullValue())
                .body("last_page", notNullValue())
                .body("per_page", notNullValue())
                .body("total", notNullValue())
                .body("data", instanceOf(List.class));
    }

    @Test
    @DisplayName("User : Get all users : Unauthorized 401 - Invalid Token")
    public void testUserGetAllUsersUnauthorized401_InvalidToken() {
        int  pageNumber = new Random().nextInt(10) - 5;

        given()
                .header("Authorization", "Bearer ABCDEFGHIJKLMNOPQRSTUVWXYZ")
                .pathParam("pageNumber", pageNumber)
                .when()
                .get("/users?page={pageNumber}")
                .then()
                .log().all()
                .statusCode(401)
                .body("message", equalTo("Unauthorized"));
    }

    @Test
    @DisplayName("User : Get all users : Not Found 404 - Invalid Endpoint")
    public void testUserGetAllUsersNotFound404_InvalidEndpoint() {
        int pageNumber = new Random().nextInt(10) - 5;

        given()
                .header("Authorization", "Bearer " + bearerToken)
                .pathParam("pageNumber", pageNumber)
                .when()
                .get("/user?page={pageNumber}")
                .then()
                .log().all()
                .statusCode(404)
                .body("message", equalTo("Resource not found"));
    }

}

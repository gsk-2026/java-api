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
public class UserSearchTest extends UserBaseTest {

    @Test
    @DisplayName("User : Get users matching the search query : Success 200")
    public void testUserSearchSuccess200() {
        String query = "first_name";
        int pageNumber = new Random().nextInt(10) - 5;

        given()
                .header("Authorization", "Bearer " + bearerToken)
                .pathParam("query", query)
                .pathParam("pageNumber", pageNumber)
                .when()
                .get("/users?q={query}&page={pageNumber}")
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
    @DisplayName("User : Get users matching the search query : Status 200 - Empty Query")
    public void testUserSearchStatus200_EmptyQ() {
        String query = "";
        int pageNumber = new Random().nextInt(10) - 5;

        given()
                .header("Authorization", "Bearer " + bearerToken)
                .pathParam("query", query)
                .pathParam("pageNumber", pageNumber)
                .when()
                .get("/users?q={query}&page={pageNumber}")
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
    @DisplayName("User : Get users matching the search query : Unauthorized 401 - Invalid Token")
    public void testUserSearchUnauthorized401_InvalidToken() {
        String query = "last_name";
        int pageNumber = new Random().nextInt(10) - 5;

        given()
                .header("Authorization", "Bearer ")
                .pathParam("query", query)
                .pathParam("pageNumber", pageNumber)
                .when()
                .get("/users?q={query}&page={pageNumber}")
                .then()
                .log().all()
                .statusCode(401)
                .body("message", equalTo("Unauthorized"));
    }

}

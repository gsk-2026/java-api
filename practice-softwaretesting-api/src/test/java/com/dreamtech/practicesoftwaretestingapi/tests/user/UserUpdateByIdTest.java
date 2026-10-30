package com.dreamtech.practicesoftwaretestingapi.tests.user;

import com.dreamtech.practicesoftwaretestingapi.model.UserUpdateRequest;
import com.dreamtech.practicesoftwaretestingapi.model.UserUpdateResponse;
import com.dreamtech.practicesoftwaretestingapi.tests.base.UserBaseTest;
import com.dreamtech.practicesoftwaretestingapi.utils.TestStatusLogger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;


@ExtendWith(TestStatusLogger.class)
public class UserUpdateByIdTest extends UserBaseTest {

    @Test
    @DisplayName("User : Update user by Id : Success 200")
    public void testUserUpdateByIdSuccess200() {
        String prefix = UUID.randomUUID().toString().substring(0, 8) + "-";
        UserUpdateRequest updateRequestBody = UserUpdateRequest.builder()
                .first_name(prefix + "John")
                .last_name(prefix + "Doe")
                .dob("2000-01-01")
                .address(prefix + "Street 1")
                .city(prefix + "City")
                .state(prefix + "State")
                .country(prefix + "Country")
                .postcode("12345")
                .phone("0-" + "123456789")
                .email(prefix + "john@doe.example")
                .password(prefix + "super-secret")
                .build();

        given()
                .header("Authorization", "Bearer " + bearerToken)
                .pathParam("userId", userId)
                .body(updateRequestBody)
                .when()
                .put("/users/{userId}")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(true))
                .extract()
                .as(UserUpdateResponse.class);
    }

    @Test
    @DisplayName("User : Update user by Id : Success 200 - Missing optional field")
    public void testUserUpdateByIdSuccess200_OptionalFieldMissing() {
        String prefix = UUID.randomUUID().toString().substring(0, 8) + "-";
        UserUpdateRequest updateRequestBody = UserUpdateRequest.builder()
                .first_name(prefix + "John")
                .last_name(prefix + "Doe")
                .dob("2000-01-01")
                .address(prefix + "Street 1")
                .city(prefix + "City")
                //.state(prefix + "State")
                .country(prefix + "Country")
                //.postcode("12345")
                //.phone("0-" + "123456789")
                .email(prefix + "john@doe.example")
                //.password(prefix + "super-secret")
                .build();

        given()
                .header("Authorization", "Bearer " + bearerToken)
                .pathParam("userId", userId)
                .body(updateRequestBody)
                .when()
                .put("/users/{userId}")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("User : Update user by Id : Unauthorized 401 - Invalid Token")
    public void testUserUpdateByIdUnauthorized401_InvalidToken() {
        String prefix = UUID.randomUUID().toString().substring(0, 8) + "-";
        UserUpdateRequest updateRequestBody = UserUpdateRequest.builder()
                .first_name(prefix + "John")
                .last_name(prefix + "Doe")
                .dob("2000-01-01")
                .address(prefix + "Street 1")
                .city(prefix + "City")
                .state(prefix + "State")
                .country(prefix + "Country")
                .postcode("12345")
                .phone("0-" + "123456789")
                .email(prefix + "john@doe.example")
                .password(prefix + "super-secret")
                .build();

        given()
                .header("Authorization", "Bearer ABCDEFGHIJKLMNOPQRSTUVWXYZ")
                .pathParam("userId", userId)
                .body(updateRequestBody)
                .when()
                .put("/users/{userId}")
                .then()
                .log().all()
                .statusCode(401)
                .body("message", equalTo("Unauthorized"));
    }

    @Test
    @DisplayName("User : Update user by Id : Forbidden 403 - Invalid UserId")
    public void testUserUpdateByIdIForbidden403_InvalidUserId() {
        String prefix = UUID.randomUUID().toString().substring(0, 8) + "-";
        UserUpdateRequest updateRequestBody = UserUpdateRequest.builder()
                .first_name(prefix + "John")
                .last_name(prefix + "Doe")
                .dob("2000-01-01")
                .address(prefix + "Street 1")
                .city(prefix + "City")
                .state(prefix + "State")
                .country(prefix + "Country")
                .postcode("12345")
                .phone("0-" + "123456789")
                .email(prefix + "john@doe.example")
                .password(prefix + "super-secret")
                .build();

        given()
                .header("Authorization", "Bearer " + bearerToken)
                .pathParam("userId", 123456)
                .body(updateRequestBody)
                .when()
                .put("/users/{userId}")
                .then()
                .log().all()
                .statusCode(403)
                .body("error", equalTo("You can only update your own data."));
    }

    @Test
    @DisplayName("User : Update user by Id : Method Not Allowed 405")
    public void testUserUpdateByIdMethodNotAllowed405() {
        String prefix = UUID.randomUUID().toString().substring(0, 8) + "-";
        UserUpdateRequest updateRequestBody = UserUpdateRequest.builder()
                .first_name(prefix + "John")
                .last_name(prefix + "Doe")
                .dob("2000-01-01")
                .address(prefix + "Street 1")
                .city(prefix + "City")
                .state(prefix + "State")
                .country(prefix + "Country")
                .postcode("12345")
                .phone("0-" + "123456789")
                .email(prefix + "john@doe.example")
                .password(prefix + "super-secret")
                .build();

        given()
                .header("Authorization", "Bearer " + bearerToken)
                .pathParam("userId", userId)
                .body(updateRequestBody)
                .when()
                .patch("/users/{userId}")
                .then()
                .log().all()
                .statusCode(405)
                .body("message", equalTo("Method is not allowed for the requested route"));
    }

    @Test
    @DisplayName("User : Update user by Id : Unprocessable Content 422 - Invalid Request")
    public void testUserUpdateByIdUnprocessable422_InvalidRequest() {
        String prefix = UUID.randomUUID().toString().substring(0, 8) + "-";
        UserUpdateRequest updateRequestBody = UserUpdateRequest.builder()
                //.first_name(prefix + "John")
                //.last_name(prefix + "Doe")
                .dob("2000-01-01")
                //.address(prefix + "Street 1")
                //.city(prefix + "City")
                .state(prefix + "State")
                //.country(prefix + "Country")
                .postcode("12345")
                .phone("0-" + "123456789")
                //.email(prefix + "john@doe.example")
                .password(prefix + "super-secret")
                .build();

        given()
                .header("Authorization", "Bearer " + bearerToken)
                .pathParam("userId", userId)
                .body(updateRequestBody)
                .when()
                .put("/users/{userId}")
                .then()
                .log().all()
                .statusCode(422)
                .body("first_name[0]", equalTo("The first name field is required."))
                .body("last_name[0]", equalTo("The last name field is required."))
                .body("address[0]", equalTo("The address field is required."))
                .body("city[0]", equalTo("The city field is required."))
                .body("country[0]", equalTo("The country field is required."))
                .body("email[0]", equalTo("The email field is required."));
    }

    @Test
    @DisplayName("User : Update user by Id : Something went wrong 500 - DOB Missing")
    public void testUserUpdateByIdWentWrong500_DobMissing() {
        String prefix = UUID.randomUUID().toString().substring(0, 8) + "-";
        UserUpdateRequest updateRequestBody = UserUpdateRequest.builder()
                .first_name(prefix + "John")
                .last_name(prefix + "Doe")
                //.dob("2000-01-01")
                .address(prefix + "Street 1")
                .city(prefix + "City")
                .state(prefix + "State")
                .country(prefix + "Country")
                .postcode("12345")
                .phone("0-" + "123456789")
                .email(prefix + "john@doe.example")
                .password(prefix + "super-secret")
                .build();

        given()
                .header("Authorization", "Bearer " + bearerToken)
                .pathParam("userId", userId)
                .body(updateRequestBody)
                .when()
                .put("/users/{userId}")
                .then()
                .log().all()
                .statusCode(500)
                .body("message", equalTo("Something went wrong"));
    }

}

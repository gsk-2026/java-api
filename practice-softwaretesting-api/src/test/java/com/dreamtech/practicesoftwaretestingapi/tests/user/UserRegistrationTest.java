package com.dreamtech.practicesoftwaretestingapi.tests.user;

import com.dreamtech.practicesoftwaretestingapi.model.UserRegisterRequest;
import com.dreamtech.practicesoftwaretestingapi.model.UserRegisterResponse;
import com.dreamtech.practicesoftwaretestingapi.tests.base.UserBaseTest;
import com.dreamtech.practicesoftwaretestingapi.utils.TestStatusLogger;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


@ExtendWith(TestStatusLogger.class)
public class UserRegistrationTest extends UserBaseTest {

    @Test
    @DisplayName("User : Register new user : Success 201")
    public void testUserRegistrationSuccess201() {
        String userEmail = "user.email." + UUID.randomUUID().toString().substring(0, 8) + "@toolshop.com";
        UserRegisterRequest requestBody = UserRegisterRequest.builder()
                .first_name("Jane")
                .last_name("Doe")
                .dob("1992-05-15")
                .address("456 Test Ave")
                .city("Chicago")
                .state("IL")
                .country("US")
                .postcode("67890")
                .phone("888-123-4567")
                .email(userEmail)
                .password("Welcome123!")
                .build();

        Response registerResponse = given()
                .body(requestBody)
                .when()
                .post("/users/register");

        registerResponse.then()
                .log().all()
                .statusCode(201)
                .body("first_name", equalTo(requestBody.first_name()))
                .body("last_name", equalTo(requestBody.last_name()))
                .body("dob", equalTo(requestBody.dob()))
                .body("address", equalTo(requestBody.address()))
                .body("city", equalTo(requestBody.city()))
                .body("state", equalTo(requestBody.state()))
                .body("country", equalTo(requestBody.country()))
                .body("postcode", equalTo(requestBody.postcode()))
                .body("phone", equalTo(requestBody.phone()))
                .body("email", equalTo(requestBody.email()))
                .body("password", nullValue())
                .body("created_at", notNullValue())
                .body("id", greaterThan(0))
                .extract()
                .as(UserRegisterResponse.class);

        userId = registerResponse.jsonPath().getInt("id");
    }

    @Test
    @DisplayName("User : Register new user : Not Found 404 - Invalid Endpoint")
    public void testUserRegistrationNotFound404_InvalidEndpoint() {
        String userEmail = "user.email." + UUID.randomUUID().toString().substring(0, 8) + "@toolshop.com";
        UserRegisterRequest requestBody = UserRegisterRequest.builder()
                .first_name("Jane")
                .last_name("Doe")
                .dob("1992-05-15")
                .address("456 Test Ave")
                .city("Chicago")
                .state("IL")
                .country("US")
                .postcode("67890")
                .phone("888-123-4567")
                .email(userEmail)
                .password("Welcome123!")
                .build();

        given()
                .body(requestBody)
                .when()
                .post("/user/register")
                .then()
                .statusCode(404)
                .body("message", equalTo("Resource not found"));
    }

    @Test
    @DisplayName("User : Register new user : Conflict 409 - Double Registration")
    public void testUserRegistrationConflict409_DoubleRegistration() {
        String userEmail = "user.email." + UUID.randomUUID().toString().substring(0, 8) + "@toolshop.com";
        UserRegisterRequest requestBody = UserRegisterRequest.builder()
                .first_name("Jane")
                .last_name("Doe")
                .dob("1992-05-15")
                .address("456 Test Ave")
                .city("Chicago")
                .state("IL")
                .country("US")
                .postcode("67890")
                .phone("888-123-4567")
                .email(userEmail)
                .password("Welcome123!")
                .build();

        // first registration
        given()
                .body(requestBody)
                .when()
                .post("/users/register")
                .then()
                //.log().all()
                .statusCode(201);

        // second registration
        given()
                .body(requestBody)
                .when()
                .post("/users/register")
                .then()
                .log().all()
                .statusCode(409)
                .body("email[0]", equalTo("A customer with this email address already exists."));
    }

    @Test
    @DisplayName("User : Register new user : Unprocessable Content 422")
    public void testUserRegistrationUnprocessable422_InvalidRequest() {
        UserRegisterRequest requestBody = UserRegisterRequest.builder().build();

        given()
                .body(requestBody)
                .when()
                .post("/users/register")
                .then()
                .log().all()
                .statusCode(422)
                .body("first_name[0]", equalTo("The first name field is required."))
                .body("last_name[0]", equalTo("The last name field is required."))
                .body("address[0]", equalTo("The address field is required."))
                .body("city[0]", equalTo("The city field is required."))
                .body("state[0]", equalTo("The state field must be a string."))
                .body("country[0]", equalTo("The country field is required."))
                .body("postcode[0]", equalTo("The postcode field must be a string."))
                .body("phone[0]", equalTo("The phone field must be a string."))
                .body("dob[0]", equalTo("The dob field is required."))
                .body("email[0]", equalTo("The email field is required."))
                .body("password[0]", equalTo("The password field is required."));
    }

}
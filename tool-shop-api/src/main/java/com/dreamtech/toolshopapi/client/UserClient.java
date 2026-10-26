package com.dreamtech.toolshopapi.client;

import com.dreamtech.toolshopapi.model.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.Getter;

import java.util.UUID;
import static io.restassured.RestAssured.given;


@Getter
public class UserClient extends BaseClient {

    public Response register(UserRegisterRequest request) {
        if (request != null) {
            first_name = request.first_name();
            last_name = request.last_name();
            dob = request.dob();
            address = request.address();
            city = request.city();
            state = request.state();
            postcode = request.postcode();
            country = request.country();
            phone = request.phone();
            email = request.email();
            password = request.password();
        }

        Response response = given()
                .body(request)
                .when()
                .post("/users/register");

        if (response.statusCode() == 201) {
            created_at = response.jsonPath().getString("created_at");
            userId = response.jsonPath().getInt("id");
        }

        return response;
    }

    public Response login(UserLoginRequest request) {
        Response response = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/users/login");

        if (response.statusCode() == 200) {
            email = request.email();
            password = request.password();
            token_type = response.jsonPath().getString("token_type");
            bearerToken = response.jsonPath().getString("access_token");
        }

        return response;
    }

    public Response getInfoByMe() {
        return given()
                .header("Authorization", "Bearer " + bearerToken)
                .when()
                .get("/users/me");
    }

    public Response getTokenInfo() {
        Response response = given()
                .header("Authorization", "Bearer " + bearerToken)
                .when()
                .get("/users/refresh");

        bearerToken = response.jsonPath().getString("access_token");

        return response;
    }

    public Response getAllUsers(int pageNumber) {
        return given()
                .header("Authorization", "Bearer " + bearerToken)
                .pathParam("pageNumber", pageNumber)
                .when()
                .get("/users?page={pageNumber}");
    }

    public Response getInfoById() {
        return given()
                .header("Authorization", "Bearer " + bearerToken)
                .pathParam("userId", userId)
                .when()
                .get("/users/{userId}");
    }

    public Response searchUser(String query, int pageNumber) {
        return given()
                .header("Authorization", "Bearer " + bearerToken)
                .pathParam("query", query)
                .pathParam("pageNumber", pageNumber)
                .when()
                .get("/users?q={query}&page={pageNumber}");
    }

    public Response updateById(UserUpdateRequest request) {
        Response response = given()
                .header("Authorization", "Bearer " + bearerToken)
                .pathParam("userId", userId)
                .body(request)
                .when()
                .put("/users/{userId}");

        if (response.statusCode() == 200) {
            if (request.first_name() != null) {
                first_name = request.first_name();
            }
            if (request.last_name() != null) {
                last_name = request.last_name();
            }
            if (request.address() != null) {
                address = request.address();
            }
            if (request.city() != null) {
                city = request.city();
            }
            if (request.state() != null) {
                state = request.state();
            }
            if (request.country() != null) {
                country = request.country();
            }
            if (request.postcode() != null) {
                postcode = request.postcode();
            }
            if (request.phone() != null) {
                phone = request.phone();
            }
            if (request.dob() != null) {
                dob = request.dob();
            }
            if (request.email() != null) {
                email = request.email();
            }
            if (request.password() != null) {
                password = request.password();
            }
        }

        return response;
    }

    public Response logout() {
        return given()
                .header("Authorization", "Bearer " + bearerToken)
                .when()
                .get("/users/logout");
    }


    /******
     P R I V A T E
     ******/
    private String first_name = "John";
    private String last_name = "Doe";
    private String dob = "1970-01-01";
    private String address = "Street 1";
    private String city = "City";
    private String state = "State";
    private String postcode = "1234AA";
    private String country = "Country";
    private String phone = "0987654321";
    private String email = "john.doe" + UUID.randomUUID().toString().substring(0, 8) + "@toolshop.com";
    private String password = "super-secret";
    private String created_at = null;
    private Integer userId = -1;
    private String token_type = null;
    private String bearerToken = null;

}
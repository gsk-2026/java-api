package com.dreamtech.toolshopapi.client;

import com.dreamtech.toolshopapi.model.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.greaterThan;
import org.junit.jupiter.api.Test;


public class UserClientTest {

    private static UserClient userClient;

    @BeforeEach
    public void setUp() {
        userClient = new UserClient();

        String userEmail = "user.test.email." + UUID.randomUUID().toString().substring(0, 8) + "@toolshop.com";
        UserRegisterRequest registerRequestBody = UserRegisterRequest.builder()
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

        Response response = userClient.register(registerRequestBody);

       if (response.statusCode() == 201) {
           UserLoginRequest loginRequest = UserLoginRequest.builder()
                   .email(userClient.getEmail())
                   .password(userClient.getPassword())
                   .build();

           response = userClient.login(loginRequest);
       }

       if (response.statusCode() == 200) {
           response = userClient.getInfoByMe();
           response.then()
                   //.log().all()
                   .statusCode(200);
       }
    }


    @Test
    @DisplayName("UserClient : store new user : Success")
    public void testUserRegistrationSuccess201() {
        String userEmail = "user.register.email." + UUID.randomUUID().toString().substring(0, 8) + "@toolshop.com";
        UserRegisterRequest request = UserRegisterRequest.builder()
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
                .password("Secret-Password")
                .build();

        Response response = userClient.register(request);

        response.then()
                .log().all()
                .statusCode(201)
                .body("first_name", equalTo(request.first_name()))
                .body("last_name", equalTo(request.last_name()))
                .body("dob", equalTo(request.dob()))
                .body("address", equalTo(request.address()))
                .body("city", equalTo(request.city()))
                .body("state", equalTo(request.state()))
                .body("country", equalTo(request.country()))
                .body("postcode", equalTo(request.postcode()))
                .body("phone", equalTo(request.phone()))
                .body("email", equalTo(request.email()))
                .body("password", nullValue())
                .body("created_at", notNullValue())
                .body("id", greaterThan(0))
                .extract()
                .as(UserRegisterResponse.class);
    }

    @Test
    @DisplayName("UserClient : login customer : Success")
    public void testUserLoginSuccess200() {
        UserLoginRequest request = UserLoginRequest.builder()
                .email("customer@practicesoftwaretesting.com")
                .password("welcome01")
                .build();

        Response response = userClient.login(request);

        response.then()
                .log().all()
                .statusCode(200)
                .body("access_token", equalTo(userClient.getBearerToken()))
                .body("token_type", notNullValue())
                .body("expires_in", greaterThan(0))
                .extract()
                .as(UserLoginResponse.class);
    }

    @Test
    @DisplayName("UserClient : retrieve current customer info : Success")
    public void testUserGetInfoByMeSuccess200() {
        Response response = userClient.getInfoByMe();

        response.then()
                .log().all()
                .statusCode(200)
                .body("id", equalTo(userClient.getUserId()))
                .body("first_name", equalTo(userClient.getFirst_name()))
                .body("last_name", equalTo(userClient.getLast_name()))
                .body("dob", equalTo(userClient.getDob()))
                .body("address", equalTo(userClient.getAddress()))
                .body("city", equalTo(userClient.getCity()))
                .body("state", equalTo(userClient.getState()))
                .body("postcode", equalTo(userClient.getPostcode()))
                .body("country", equalTo(userClient.getCountry()))
                .body("phone", equalTo(userClient.getPhone()))
                .body("email", equalTo(userClient.getEmail()))
                .body("created_at", notNullValue())
                .extract()
                .as(UserGetResponse.class);
    }

    @Test
    @DisplayName("UserClient : retrieve all users : Success")
    public void testUserGetAllUsersSuccess200() {
        int pageNum = 1;
        Response response = userClient.getAllUsers(pageNum);
        response.then()
                .log().all()
                .statusCode(200)
                .body("current_page", equalTo(pageNum > 0 ? pageNum : 1))
                //.body("from", notNullValue())
                //.body("to", notNullValue())
                .body("last_page", notNullValue())
                .body("per_page", notNullValue())
                .body("total", notNullValue())
                .body("data", instanceOf(List.class));
    }

    @Test
    @DisplayName("UserClient : retrieve specific user : Success")
    public void testUserGetInfoByIdSuccess200() {
        Response getResponse = userClient.getInfoById();

        getResponse.then()
                .log().all()
                .statusCode(200)
                .body("id", equalTo(userClient.getUserId()))
                .body("first_name", equalTo(userClient.getFirst_name()))
                .body("last_name", equalTo(userClient.getLast_name()))
                .body("dob", equalTo(userClient.getDob()))
                .body("address", equalTo(userClient.getAddress()))
                .body("city", equalTo(userClient.getCity()))
                .body("country", equalTo(userClient.getCountry()))
                .body("phone", equalTo(userClient.getPhone()))
                .body("email", equalTo(userClient.getEmail()))
                .body("created_at", notNullValue())
                .extract()
                .as(UserGetResponse.class);
    }

    @Test
    @DisplayName("UserClient : retrieve specific users matching the search query : Success")
    public void testUserSearchUserSuccess200() {
        String q = "XYZ";
        int pageNum = 10;
        Response response = userClient.searchUser(q, pageNum);

        response.then()
                .log().all()
                .statusCode(200)
                .body("current_page", equalTo(pageNum > 0 ? pageNum : 1))
                //.body("from", notNullValue())
                //.body("to", notNullValue())
                .body("last_page", notNullValue())
                .body("per_page", notNullValue())
                .body("total", notNullValue())
                .body("data", instanceOf(List.class));
    }

    @Test
    @DisplayName("UserClient : update specific user : Success")
    public void testUserUpdateByIdSuccess200() {
        String prefix = UUID.randomUUID().toString().substring(0, 8) + "-";

        UserUpdateRequest request = UserUpdateRequest.builder()
                .first_name(prefix + "John")
                .last_name(prefix + "Doe")
                .dob("2000-01-01")
                .address(prefix + "Street 1")
                .city(prefix + "City")
                .state(prefix + "State")
                .country(prefix + "Country")
                .postcode("12345")
                .phone("0-" + "123456789")
                .email(userClient.getEmail())
                .password(userClient.getPassword())
                .build();

        Response response = userClient.updateById(request);

        response.then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(true))
                .extract()
                .as(UserUpdateResponse.class);
    }

    @Test
    @DisplayName("UserClient : retrieve a refreshed token : Success")
    public void testUserGetTokenInfoSuccess200() {
        Response meResponse = userClient.getTokenInfo();

        meResponse.then()
                .log().all()
                .statusCode(200)
                .body("access_token", equalTo(userClient.getBearerToken()))
                .body("token_type", equalTo("bearer"))
                .body("expires_in", greaterThan(0))
                .extract()
                .as(UserRefreshResponse.class);
    }

    @Test
    @DisplayName("UserClient : logout - invalidate the token : Success")
    public void testUserLogoutSuccess200() {
        Response meResponse = userClient.logout();

        meResponse.then()
                .log().all()
                .statusCode(200)
                .body("message", equalTo("Successfully logged out"))
                .extract()
                .as(UserLogoutResponse.class);
    }

}

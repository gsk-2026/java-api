package com.dreamtech.toolshopapi.client;

import com.dreamtech.toolshopapi.model.*;
import com.dreamtech.toolshopapi.utils.TestStatusLogger;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.greaterThan;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


@ExtendWith(TestStatusLogger.class)
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
           response = userClient.getInfoByMe(null);
           response.then()
                   //.log().all()
                   .statusCode(200);
       }
    }


    @Test
    @DisplayName("UserClient : store new user : Success 201")
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

        userClient.register(request).then()
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
    @DisplayName("UserClient : store new user : Conflict 409 - duplicate register")
    public void testUserRegistrationConflict409_duplicateRegister() {
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

        userClient.register(request).then()
                .log().all()
                .statusCode(201);

        userClient.register(request).then()
                .log().all()
                .statusCode(409)
                .body("email[0]", equalTo("A customer with this email address already exists."));
    }

    @Test
    @DisplayName("UserClient : store new user : Unprocessable Content 422 - empty request")
    public void testUserRegistrationUnprocessable422_emptyRequest() {
        UserRegisterRequest request = UserRegisterRequest.builder().build();

        userClient.register(request).then()
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


    @Test
    @DisplayName("UserClient : login customer : Success 200")
    public void testUserLoginSuccess200() {
        UserLoginRequest request = UserLoginRequest.builder()
                .email("customer@practicesoftwaretesting.com")
                .password("welcome01")
                .build();

        userClient.login(request).then()
                .log().all()
                .statusCode(200)
                .body("access_token", equalTo(userClient.getBearerToken()))
                .body("token_type", notNullValue())
                .body("expires_in", greaterThan(0))
                .extract()
                .as(UserLoginResponse.class);
    }

    @Test
    @DisplayName("UserClient : login customer : Success 200 - duplicate login")
    public void testUserLoginSuccess200_duplicateLogin() {
        UserLoginRequest request = UserLoginRequest.builder()
                .email(userClient.getEmail())
                .password(userClient.getPassword())
                .build();

        userClient.login(request).then()
                .log().all()
                .statusCode(200)
                .body("access_token", equalTo(userClient.getBearerToken()))
                .body("token_type", notNullValue())
                .body("expires_in", greaterThan(0))
                .extract()
                .as(UserLoginResponse.class);

        userClient.login(request).then()
                .log().all()
                .statusCode(200)
                .body("access_token", equalTo(userClient.getBearerToken()))
                .body("token_type", notNullValue())
                .body("expires_in", greaterThan(0))
                .extract()
                .as(UserLoginResponse.class);
    }

    @Test
    @DisplayName("UserClient : login customer : Success 200 - post logout")
    public void testUserDoubleLoginSuccess200_postLogout() {
        userClient.logout().then()
                .statusCode(200)
                .body("message", equalTo("Successfully logged out"));

        UserLoginRequest request = UserLoginRequest.builder()
                .email(userClient.getEmail())
                .password(userClient.getPassword())
                .build();

        userClient.login(request).then()
                .log().all()
                .statusCode(200)
                .body("access_token", equalTo(userClient.getBearerToken()))
                .body("token_type", notNullValue())
                .body("expires_in", greaterThan(0))
                .extract()
                .as(UserLoginResponse.class);
    }

    @Test
    @DisplayName("UserClient : login customer : Unauthorized 401 - empty request")
    public void testUserLoginUnauthorized401_emptyRequest() {
        UserLoginRequest request = UserLoginRequest.builder().build();

        userClient.login(request).then()
                .log().all()
                .statusCode(401)
                .body("error", equalTo("Unauthorized"));
    }

    @Test
    @DisplayName("UserClient : login customer : Unauthorized 401 - invalid email/pswd")
    public void testUserLoginUnauthorized401_email() {
        UserLoginRequest request = UserLoginRequest.builder()
                .email("invalid.email.com")
                .password("invalid_pswd")
                .build();

        userClient.login(request).then()
                .log().all()
                .statusCode(401)
                .body("error", equalTo("Unauthorized"));
    }


    @Test
    @DisplayName("UserClient : retrieve current customer info : Success 200")
    public void testUserGetInfoByMeSuccess200() {
       userClient.getInfoByMe().then()
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
    @DisplayName("UserClient : retrieve current customer info : Unauthorized 401 - invalid token")
    public void testUserGetInfoByMeUnauthorized401_invalidToken() {
        String token = "invalid_token";
        userClient.getInfoByMe(token).then()
                .log().all()
                .statusCode(401)
                .body("message", equalTo("Unauthorized"));
    }

    @Test
    @DisplayName("UserClient : retrieve current customer info : Unauthorized 401 - post logout")
    public void testUserGetInfoByMeUnauthorized401_postLogout() {
        userClient.logout().then()
                .statusCode(200)
                .body("message", equalTo("Successfully logged out"));

         userClient.getInfoByMe().then()
                .log().all()
                .statusCode(401)
                .body("message", equalTo("Unauthorized"));
    }


    @Test
    @DisplayName("UserClient : retrieve all users : Success 200")
    public void testUserGetAllUsersSuccess200() {
        int pageNum = new Random().nextInt(10) - 5;
        userClient.getAllUsers(pageNum).then()
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
    @DisplayName("UserClient : retrieve all users : Unauthorized 401 - invalid token")
    public void testUserGetAllUsersUnauthorized401_invalidToken() {
        String token = "invalid_token";
        int pageNum = new Random().nextInt(10) - 5;
        userClient.getAllUsers(pageNum, token).then()
                .log().all()
                .statusCode(401)
                .body("message", equalTo("Unauthorized"));
    }

    @Test
    @DisplayName("UserClient : retrieve all users : Unauthorized 401 - post logout")
    public void testUserGetAllUsersUnauthorized401_postLogout() {
        userClient.logout().then()
                .statusCode(200)
                .body("message", equalTo("Successfully logged out"));

        int pageNum = new Random().nextInt(10) - 5;
        userClient.getAllUsers(pageNum).then()
                .log().all()
                .statusCode(401)
                .body("message", equalTo("Unauthorized"));
    }


    @Test
    @DisplayName("UserClient : retrieve specific user : Success 200")
    public void testUserGetInfoByIdSuccess200() {
        userClient.getInfoById().then()
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
    @DisplayName("UserClient : retrieve specific user : Unauthorized 401 - post logout")
    public void testUserGetInfoByIdUnauthorized401_postLogout() {
        userClient.logout().then()
                .statusCode(200)
                .body("message", equalTo("Successfully logged out"));

        userClient.getInfoById().then()
                .log().all()
                .statusCode(401)
                .body("message", equalTo("Unauthorized"));
    }

    @Test
    @DisplayName("UserClient : retrieve specific user : Unauthorized 401 - invalid token")
    public void testUserGetInfoByIdUnauthorized401_invalidToken() {
        String token = "invalid_token";
        userClient.getInfoById(token).then()
                .log().all()
                .statusCode(401)
                .body("message", equalTo("Unauthorized"));
    }

    @Test
    @DisplayName("UserClient : retrieve specific user : Not Found 404 - invalid Id")
    public void testUserGetInfoByIdNotFound404_invalidId() {
        int id = 999888777;
        userClient.getInfoById(id).then()
                .log().all()
                .statusCode(404)
                .body("message", equalTo("Requested item not found"));
    }


    @Test
    @DisplayName("UserClient : retrieve specific users matching the search query : Success 200")
    public void testUserSearchUserSuccess200() {
        String q = "XYZ";
        int pageNum = new Random().nextInt(10) - 5;
        userClient.searchUser(q, pageNum).then()
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
    @DisplayName("UserClient : retrieve specific users matching the search query : Unauthorized 401 - post logout")
    public void testUserSearchUserUnauthorized401_postLogout() {
        userClient.logout().then()
                .statusCode(200)
                .body("message", equalTo("Successfully logged out"));

        String q = "XYZ";
        int pageNum = new Random().nextInt(10) - 5;
        userClient.searchUser(q, pageNum).then()
                .log().all()
                .statusCode(401)
                .body("message", equalTo("Unauthorized"));
    }

    @Test
    @DisplayName("UserClient : retrieve specific users matching the search query : Unauthorized 401 - invalid token")
    public void testUserSearchUserUnauthorized401_invalidToken() {
        String token = "invalid_token";
        String q = "XYZ";
        int pageNum = new Random().nextInt(10) - 5;
        userClient.searchUser(q, pageNum, token).then()
                .log().all()
                .statusCode(401)
                .body("message", equalTo("Unauthorized"));
    }


    @Test
    @DisplayName("UserClient : update specific user : Success 200")
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

        userClient.updateById(request).then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(true))
                .extract()
                .as(UserUpdateResponse.class);
    }

    @Test
    @DisplayName("UserClient : update specific user : Unauthorized 401 - post logout")
    public void testUserUpdateByIdUnauthorized401_postLogout() {
        userClient.logout().then()
                .statusCode(200)
                .body("message", equalTo("Successfully logged out"));

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

        userClient.updateById(request).then()
                .log().all()
                .statusCode(401)
                .body("message", equalTo("Unauthorized")) ;
    }

    @Test
    @DisplayName("UserClient : update specific user : Unauthorized 401 - invalid token")
    public void testUserUpdateByIdUnauthorized401_invalidToken() {
        String token = "invalid_token";
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

        userClient.updateById(request, token).then()
                .log().all()
                .statusCode(401)
                .body("message", equalTo("Unauthorized")) ;
    }

    @Test
    @DisplayName("UserClient : update specific user : Unprocessable Content 422 - empty request")
    public void testUserUpdateByIdUnprocessable422_emptyRequest() {
        UserUpdateRequest request = UserUpdateRequest.builder().build();

        userClient.updateById(request).then()
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
    @DisplayName("UserClient : retrieve a refreshed token : Success 200")
    public void testUserGetRefreshedTokenSuccess200() {
        userClient.getRefreshedToken().then()
                .log().all()
                .statusCode(200)
                .body("access_token", equalTo(userClient.getBearerToken()))
                .body("token_type", equalTo("bearer"))
                .body("expires_in", greaterThan(0))
                .extract()
                .as(UserRefreshResponse.class);
    }

    @Test
    @DisplayName("UserClient : retrieve a refreshed token : Unauthorized 401 - post logout")
    public void testUserGetRefreshedTokenUnauthorized401_postLogout() {
        userClient.logout().then()
                .statusCode(200)
                .body("message", equalTo("Successfully logged out"));

        userClient.getRefreshedToken().then()
                .log().all()
                .statusCode(401)
                .body("message", equalTo("Token is not valid"));
    }

    @Test
    @DisplayName("UserClient : retrieve a refreshed token : Server Error 500 - invalid token")
    public void testUserGetRefreshedTokenServerError500_invalidToken() {
        String token = "invalid_token";
        userClient.getRefreshedToken(token).then()
                .log().all()
                .statusCode(500)
                .body("message", equalTo("Server Error"));
    }


    @Test
    @DisplayName("UserClient : logout : Success 200")
    public void testUserLogoutSuccess200() {
        userClient.logout().then()
                .log().all()
                .statusCode(200)
                .body("message", equalTo("Successfully logged out"))
                .extract()
                .as(UserLogoutResponse.class);
    }

    @Test
    @DisplayName("UserClient : logout : Unauthorized 401 - post logout")
    public void testUserLogoutUnauthorized401_postLogout() {
        userClient.logout().then()
                //.log().all()
                .statusCode(200)
                .body("message", equalTo("Successfully logged out"))
                .extract()
                .as(UserLogoutResponse.class);

        userClient.logout().then()
                .log().all()
                .statusCode(401)
                .body("message", equalTo("Unauthorized"));
    }

    @Test
    @DisplayName("UserClient : logout : Unauthorized 401 - invalid token")
    public void testUserLogoutUnauthorized401_invalidToken() {
        String token = "invalid_token";

        userClient.logout(token).then()
                .log().all()
                .statusCode(401)
                .body("message", equalTo("Unauthorized"));
    }

}

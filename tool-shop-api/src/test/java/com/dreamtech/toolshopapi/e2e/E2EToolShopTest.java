package com.dreamtech.toolshopapi.e2e;

import com.dreamtech.toolshopapi.client.InvoiceClient;
import com.dreamtech.toolshopapi.client.UserClient;
import com.dreamtech.toolshopapi.model.*;
import com.dreamtech.toolshopapi.utils.TestStatusLogger;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.nullValue;


@ExtendWith(TestStatusLogger.class)
public class E2EToolShopTest {

    public static class StepFailure extends RuntimeException {
        public StepFailure(String message) {
            super(message);
        }
    }

    @FunctionalInterface
    public interface TestStep {
        void execute() throws Exception;
    }

    private void runStep(String stepName, TestStep step) {
        try {
            step.execute();
        } catch (AssertionError | Exception ex) {
            throw new StepFailure(String.format("Fatal error at step: %s, exception: %s", stepName, ex.getMessage()));
        }
    }


    @Test
    public void testE2EUserClientHappyPath() {
        UserClient userClient = new UserClient();

        try {
            runStep("E2E_UserClient_Happy_Path: step_store_new_user", () -> {
                String userEmail = "user.email." + UUID.randomUUID().toString().substring(0, 8) + "@toolshop.com";
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
            });

            runStep("E2E_UserClient_Happy_Path: step_login_cudtomer", () -> {
                UserLoginRequest request = UserLoginRequest.builder()
                        .email(userClient.getEmail())
                        .password(userClient.getPassword())
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
            });

            runStep("E2E_UserClient_Happy_Path: step_retrieve_all_users", () -> {
                int pageNum = new Random().nextInt(10) - 5;
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
            });

            runStep("E2E_UserClient_Happy_Path: step_retrieve_specific_user", () -> {
                Response response = userClient.getInfoById();
                response.then()
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
            });

            runStep("E2E_UserClient_Happy_Path: step_retrieve_specific_users_matching_the_search_query", () -> {
                String q = "XYZ";
                int pageNum = new Random().nextInt(10) - 5;
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
            });

            runStep("E2E_UserClient_Happy_Path: step_update_specific_user", () -> {
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
            });

            runStep("E2E_UserClient_Happy_Path: step_retrieve_refreshed_token", () -> {
                Response response = userClient.getRefreshedToken();
                response.then()
                        .log().all()
                        .statusCode(200)
                        .body("access_token", equalTo(userClient.getBearerToken()))
                        .body("token_type", equalTo("bearer"))
                        .body("expires_in", greaterThan(0))
                        .extract()
                        .as(UserRefreshResponse.class);
            });

            runStep("E2E_UserClient_Happy_Path: step_logout", () -> {
                Response response = userClient.logout();
                response.then()
                        .log().all()
                        .statusCode(200)
                        .body("message", equalTo("Successfully logged out"))
                        .extract()
                        .as(UserLogoutResponse.class);
            });

        } catch (StepFailure sf) {
            Assertions.fail("E2E_UserClient_Happy_Path : Experienced a fatal step failure and Skipped the rest of the steps: " + sf.getMessage());
        } finally {
            Response response = userClient.getRefreshedToken();
            if (response.statusCode() == 200) {
                String token = response.jsonPath().getString("access_token");
                if (token != null && !token.isEmpty()) {
                    userClient.logout().then()
                            .log().all()
                            .statusCode(200)
                            .body("message", equalTo("Successfully logged out"))
                            .extract()
                            .as(UserLogoutResponse.class);
                }
            }
        }
    }


    @Test
    public void testE2EUserClientEdgeCase() {
        UserClient userClient = new UserClient();

        try {
            runStep("E2E_UserClient_Edge_Case: step_store_new_user", () -> {
                String userEmail = "user.email." + UUID.randomUUID().toString().substring(0, 8) + "@toolshop.com";
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
            });

            runStep("E2E_UserClient_Edge_Case: step_retrieve_specific_user_A", () -> {
                Response response = userClient.getInfoById();
                response.then()
                        .log().all()
                        .statusCode(401)
                        .body("message", equalTo("Unauthorized"));
            });

            runStep("E2E_UserClient_Edge_Case: step_login_cudtomer", () -> {
                UserLoginRequest request = UserLoginRequest.builder()
                        .email(userClient.getEmail())
                        .password(userClient.getPassword())
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
            });

            runStep("E2E_UserClient_Edge_Case: step_retrieve_specific_user_B", () -> {
                Response response = userClient.getInfoById();
                response.then()
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
            });

            runStep("E2E_UserClient_Edge_Case: step_retrieve_refreshed_token_A", () -> {
                Response response = userClient.getRefreshedToken();
                response.then()
                        .log().all()
                        .statusCode(200)
                        .body("access_token", equalTo(userClient.getBearerToken()))
                        .body("token_type", equalTo("bearer"))
                        .body("expires_in", greaterThan(0))
                        .extract()
                        .as(UserRefreshResponse.class);
            });

            runStep("E2E_UserClient_Edge_Case: step_logout", () -> {
                Response response = userClient.logout();
                response.then()
                        .log().all()
                        .statusCode(200)
                        .body("message", equalTo("Successfully logged out"))
                        .extract()
                        .as(UserLogoutResponse.class);
            });

            runStep("E2E_UserClient_Edge_Case: step_retrieve_refreshed_token_B", () -> {
                Response response = userClient.getRefreshedToken();
                response.then()
                        .log().all()
                        .statusCode(401)
                        .body("message", equalTo("Token is not valid"));
            });

        } catch (StepFailure sf) {
            Assertions.fail("E2E_UserClient_Edge_Case : Experienced a fatal step failure and Skipped the rest of the steps: " + sf.getMessage());
        } finally {
            Response response = userClient.getRefreshedToken();
            if (response.statusCode() == 200) {
                String token = response.jsonPath().getString("access_token");
                if (token != null && !token.isEmpty()) {
                    userClient.logout().then()
                            .log().all()
                            .statusCode(200)
                            .body("message", equalTo("Successfully logged out"))
                            .extract()
                            .as(UserLogoutResponse.class);
                }
            }
        }
    }


    @Test
    public void testE2EInvoiceClientHappyPath() {
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        UserRegisterRequest userRegisterRequestBody = UserRegisterRequest.builder()
                .first_name("Jane")
                .last_name("Doe")
                .dob("1992-05-15")
                .address("456 Test Ave")
                .city("Chicago")
                .state("IL")
                .country("US")
                .postcode("67890")
                .phone("888-123-4567")
                .email("user.test.email." + uuid + "user.test.email.")
                .password("Welcome123!")
                .build();
        InvoiceClient invoiceClient = new InvoiceClient(userRegisterRequestBody);

        try {
            runStep("E2E_InvoiceClient_Happy_Path: step_store_new_invoice", () -> {
                InvoicePostRequest invoicePostRequestBody = InvoicePostRequest.builder()
                        .billing_address("billing_address:" + uuid)
                        .billing_city("billing_city:" + uuid)
                        .billing_state("billing_state:" + uuid)
                        .billing_postcode("12345-6789")
                        .billing_country("billing_country:" + uuid)
                        .payment_method("payment_method:" + uuid)
                        .payment_account_name("payment_account_name :" + uuid)
                        .payment_account_number("0987654345")
                        .total(123.45)
                        .invoicelines(List.of(
                                InvoiceModel.builder().product_id(1).unit_price(1.11).quantity(1).build(),
                                InvoiceModel.builder().product_id(2).unit_price(2.22).quantity(2).build(),
                                InvoiceModel.builder().product_id(3).unit_price(3.33).quantity(3).build()
                        ))
                        .build();

                Response response = invoiceClient.post(invoicePostRequestBody);
                response.then()
                        .log().all()
                        .statusCode(201)
                        .body("billing_address", equalTo(invoicePostRequestBody.billing_address()))
                        .body("billing_city", equalTo(invoicePostRequestBody.billing_city()))
                        .body("billing_state", equalTo(invoicePostRequestBody.billing_state()))
                        .body("billing_postcode", equalTo(invoicePostRequestBody.billing_postcode()))
                        .body("billing_country", equalTo(invoicePostRequestBody.billing_country()))
                        .body("payment_method", equalTo(invoicePostRequestBody.payment_method()))
                        .body("payment_account_name", equalTo(invoicePostRequestBody.payment_account_name()))
                        .body("payment_account_number", equalTo(invoicePostRequestBody.payment_account_number()))
                        .body("total", equalTo((float) invoicePostRequestBody.total()))
                        .body("invoice_date", notNullValue())
                        .body("invoice_number", notNullValue())
                        .body("user_id", notNullValue())
                        .body("id", notNullValue())
                        .extract()
                        .as(InvoicePostResponse.class);
            });

            runStep("E2E_InvoiceClient_Happy_Path: step_retrieve_specific_invoice", () -> {
                Response response = invoiceClient.getInvoice();
                response.then()
                        .log().all()
                        .statusCode(200)
                        .body("id", equalTo(invoiceClient.getInvoiceId()))
                        .body("user_id", notNullValue())
                        .body("invoice_number", equalTo(invoiceClient.getInvoiceNumber()))
                        .body("billing_address", equalTo(invoiceClient.getBilling_address()))
                        .body("billing_city", equalTo(invoiceClient.getBilling_city()))
                        .body("billing_state", equalTo(invoiceClient.getBilling_state()))
                        .body("billing_country", equalTo(invoiceClient.getBilling_country()))
                        .body("billing_postcode", equalTo(invoiceClient.getBilling_postcode()))
                        .body("total", equalTo((float) invoiceClient.getTotal()))
                        .body("payment_method", equalTo(invoiceClient.getPayment_method()))
                        .body("payment_account_name", equalTo(invoiceClient.getPayment_account_name()))
                        .body("payment_account_number", equalTo(invoiceClient.getPayment_account_number()))
                        .body("status", notNullValue())
                        .body("invoicelines", instanceOf(java.util.List.class))
                        .extract()
                        .as(InvoiceGetResponse.class);
            });

            runStep("E2E_InvoiceClient_Happy_Path: step_retrieve_all_invoices", () -> {
                int pageNumber = new Random().nextInt(10) - 5;
                Response response = invoiceClient.getAll(pageNumber);
                response.then()
                        .log().all()
                        .statusCode(200)
                        .body("current_page", equalTo((pageNumber > 0) ? pageNumber : 1))
                        //.body("from", notNullValue())
                        //.body("to", notNullValue())
                        .body("last_page", notNullValue())
                        .body("per_page", notNullValue())
                        .body("total", notNullValue())
                        .body("data", instanceOf(java.util.List.class));
            });

            runStep("E2E_InvoiceClient_Happy_Path: step_retrieve_specific_invoice_matching_the_search_query", () -> {
                String query = "A";
                int pageNumber = new Random().nextInt(10) - 5;
                Response response = invoiceClient.search(query, pageNumber);
                response.then()
                        .log().all()
                        .statusCode(200)
                        .body("current_page", equalTo((pageNumber > 0) ? pageNumber : 1))
                        //.body("from", notNullValue())
                        //.body("to", notNullValue())
                        .body("last_page", notNullValue())
                        .body("per_page", notNullValue())
                        .body("total", notNullValue())
                        .body("data", instanceOf(java.util.List.class));
            });

            runStep("E2E_InvoiceClient_Happy_Path: step_update_specific_invoice", () -> {
                InvoiceUpdateRequest updateRequest = InvoiceUpdateRequest.builder()
                        .user_id(invoiceClient.getUserClient().getUserId())
                        .billing_address("update billing_address")
                        .billing_city("update billing_city")
                        .billing_state("update billing_state")
                        .billing_postcode("12345-6789")
                        .billing_country("update billing_country")
                        .payment_method("Cash on Delivery payment_method")
                        .payment_account_name("John Doe payment_account_name")
                        .payment_account_number("0987654345")
                        .total(15.54)
                        .invoicelines(List.of(
                                InvoiceModel.builder().product_id(1).unit_price(1.11).quantity(1).build(),
                                InvoiceModel.builder().product_id(2).unit_price(2.22).quantity(2).build(),
                                InvoiceModel.builder().product_id(3).unit_price(3.33).quantity(3).build()
                        ))
                        .build();

                Response response = invoiceClient.update(updateRequest);
                response.then()
                        .log().all()
                        .statusCode(200)
                        .body("success", equalTo(true))
                        .extract()
                        .as(InvoiceUpdateResponse.class);
            });

            runStep("E2E_InvoiceClient_Happy_Path: step_update_invoice_status", () -> {
                InvoiceStatusUpdateRequest updateRequest = InvoiceStatusUpdateRequest.builder()
                        .status("AWAITING_FULFILLMENT")
                        .status_message("update invoice status")
                        .build();

                Response response = invoiceClient.updateStatus(updateRequest);
                response.then()
                        .log().all()
                        .statusCode(200)
                        .body("success", equalTo(true))
                        .extract()
                        .as(InvoiceStatusUpdateResponse.class);
            });

        } catch (StepFailure sf) {
            Assertions.fail("E2E_InvoiceClient_Happy_Path : Experienced a fatal step failure and Skipped the rest of the steps: " + sf.getMessage());
        } finally {
            UserClient userClient = invoiceClient.getUserClient();
            Response response = userClient.getRefreshedToken();
            if (response.statusCode() == 200) {
                String token = response.jsonPath().getString("access_token");
                if (token != null && !token.isEmpty()) {
                    userClient.logout().then()
                            .log().all()
                            .statusCode(200)
                            .body("message", equalTo("Successfully logged out"))
                            .extract()
                            .as(UserLogoutResponse.class);
                }
            }
        }
    }


    @Test
    public void testE2EInvoiceClientEdgeCase() {
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        UserRegisterRequest userRegisterRequestBody = UserRegisterRequest.builder()
                .first_name("Jane")
                .last_name("Doe")
                .dob("1992-05-15")
                .address("456 Test Ave")
                .city("Chicago")
                .state("IL")
                .country("US")
                .postcode("67890")
                .phone("888-123-4567")
                .email("user.test.email." + uuid + "user.test.email.")
                .password("Welcome123!")
                .build();
        InvoiceClient invoiceClient = new InvoiceClient(userRegisterRequestBody);

        try {
            runStep("E2E_InvoiceClient_Edge_Case: step_retrieve_all_invoices_A", () -> {
                int pageNumber = new Random().nextInt(10) - 5;
                Response response = invoiceClient.getAll(pageNumber);
                response.then()
                        .log().all()
                        .statusCode(200)
                        .body("current_page", equalTo((pageNumber > 0) ? pageNumber : 1))
                        //.body("from", notNullValue())
                        //.body("to", notNullValue())
                        .body("last_page", notNullValue())
                        .body("per_page", notNullValue())
                        .body("total", notNullValue())
                        .body("data", instanceOf(java.util.List.class));
            });

            runStep("E2E_InvoiceClient_Edge_Case: step_store_new_invoice_A", () -> {
                InvoicePostRequest invoicePostRequestBody = InvoicePostRequest.builder()
                        .billing_address("billing_address:" + uuid)
                        .billing_city("billing_city:" + uuid)
                        .billing_state("billing_state:" + uuid)
                        .billing_postcode("12345-6789")
                        .billing_country("billing_country:" + uuid)
                        .payment_method("payment_method:" + uuid)
                        .payment_account_name("payment_account_name :" + uuid)
                        .payment_account_number("0987654345")
                        .total(123.45)
                        .invoicelines(List.of(
                                InvoiceModel.builder().product_id(1).unit_price(1.11).quantity(1).build(),
                                InvoiceModel.builder().product_id(2).unit_price(2.22).quantity(2).build(),
                                InvoiceModel.builder().product_id(3).unit_price(3.33).quantity(3).build()
                        ))
                        .build();

                Response response = invoiceClient.post(invoicePostRequestBody);
                response.then()
                        .log().all()
                        .statusCode(201)
                        .body("billing_address", equalTo(invoicePostRequestBody.billing_address()))
                        .body("billing_city", equalTo(invoicePostRequestBody.billing_city()))
                        .body("billing_state", equalTo(invoicePostRequestBody.billing_state()))
                        .body("billing_postcode", equalTo(invoicePostRequestBody.billing_postcode()))
                        .body("billing_country", equalTo(invoicePostRequestBody.billing_country()))
                        .body("payment_method", equalTo(invoicePostRequestBody.payment_method()))
                        .body("payment_account_name", equalTo(invoicePostRequestBody.payment_account_name()))
                        .body("payment_account_number", equalTo(invoicePostRequestBody.payment_account_number()))
                        .body("total", equalTo((float) invoicePostRequestBody.total()))
                        .body("invoice_date", notNullValue())
                        .body("invoice_number", notNullValue())
                        .body("user_id", notNullValue())
                        .body("id", notNullValue())
                        .extract()
                        .as(InvoicePostResponse.class);
            });

            runStep("E2E_InvoiceClient_Edge_Case: step_user_client_logout", () -> {
                UserClient userClient = invoiceClient.getUserClient();
                String token = userClient.getBearerToken();
                if (token != null && !token.isEmpty()) {
                    userClient.logout().then()
                            .log().all()
                            .statusCode(200)
                            .body("message", equalTo("Successfully logged out"))
                            .extract()
                            .as(UserLogoutResponse.class);
                }
            });

            runStep("E2E_InvoiceClient_Edge_Case: step_retrieve_all_invoices_B", () -> {
                int pageNumber = new Random().nextInt(10) - 5;
                Response response = invoiceClient.getAll(pageNumber);
                response.then()
                        .log().all()
                        .statusCode(401)
                        .body("message", equalTo("Unauthorized"));
            });

            runStep("E2E_InvoiceClient_Edge_Case: step_store_new_invoice_B", () -> {
                InvoicePostRequest invoicePostRequestBody = InvoicePostRequest.builder()
                        .billing_address("billing_address:" + uuid)
                        .billing_city("billing_city:" + uuid)
                        .billing_state("billing_state:" + uuid)
                        .billing_postcode("12345-6789")
                        .billing_country("billing_country:" + uuid)
                        .payment_method("payment_method:" + uuid)
                        .payment_account_name("payment_account_name :" + uuid)
                        .payment_account_number("0987654345")
                        .total(123.45)
                        .invoicelines(List.of(
                                InvoiceModel.builder().product_id(1).unit_price(1.11).quantity(1).build(),
                                InvoiceModel.builder().product_id(2).unit_price(2.22).quantity(2).build(),
                                InvoiceModel.builder().product_id(3).unit_price(3.33).quantity(3).build()
                        ))
                        .build();

                Response response = invoiceClient.post(invoicePostRequestBody);
                response.then()
                        .log().all()
                        .statusCode(401)
                        .body("message", equalTo("Unauthorized"));
            });

            runStep("E2E_InvoiceClient_Edge_Case: step_user_client_login", () -> {
                UserClient userClient = invoiceClient.getUserClient();
                UserLoginRequest loginRequest = UserLoginRequest.builder()
                        .email(userClient.getEmail())
                        .password(userClient.getPassword())
                        .build();
                userClient.login(loginRequest).then()
                        .log().all()
                        .statusCode(200)
                        .body("access_token", notNullValue())
                        .body("token_type", equalTo("bearer"))
                        .body("expires_in", notNullValue());
            });

            runStep("E2E_InvoiceClient_Edge_Case: step_retrieve_all_invoices_C", () -> {
                int pageNumber = new Random().nextInt(10) - 5;
                Response response = invoiceClient.getAll(pageNumber);
                response.then()
                        .log().all()
                        .statusCode(200)
                        .body("current_page", equalTo((pageNumber > 0) ? pageNumber : 1))
                        //.body("from", notNullValue())
                        //.body("to", notNullValue())
                        .body("last_page", notNullValue())
                        .body("per_page", notNullValue())
                        .body("total", notNullValue())
                        .body("data", instanceOf(java.util.List.class));
            });

            runStep("E2E_InvoiceClient_Edge_Case: step_store_new_invoice_C", () -> {
                InvoicePostRequest invoicePostRequestBody = InvoicePostRequest.builder()
                        .billing_address("billing_address:" + uuid)
                        .billing_city("billing_city:" + uuid)
                        .billing_state("billing_state:" + uuid)
                        .billing_postcode("12345-6789")
                        .billing_country("billing_country:" + uuid)
                        .payment_method("payment_method:" + uuid)
                        .payment_account_name("payment_account_name :" + uuid)
                        .payment_account_number("0987654345")
                        .total(123.45)
                        .invoicelines(List.of(
                                InvoiceModel.builder().product_id(1).unit_price(1.11).quantity(1).build(),
                                InvoiceModel.builder().product_id(2).unit_price(2.22).quantity(2).build(),
                                InvoiceModel.builder().product_id(3).unit_price(3.33).quantity(3).build()
                        ))
                        .build();

                Response response = invoiceClient.post(invoicePostRequestBody);
                response.then()
                        .log().all()
                        .statusCode(201)
                        .body("billing_address", equalTo(invoicePostRequestBody.billing_address()))
                        .body("billing_city", equalTo(invoicePostRequestBody.billing_city()))
                        .body("billing_state", equalTo(invoicePostRequestBody.billing_state()))
                        .body("billing_postcode", equalTo(invoicePostRequestBody.billing_postcode()))
                        .body("billing_country", equalTo(invoicePostRequestBody.billing_country()))
                        .body("payment_method", equalTo(invoicePostRequestBody.payment_method()))
                        .body("payment_account_name", equalTo(invoicePostRequestBody.payment_account_name()))
                        .body("payment_account_number", equalTo(invoicePostRequestBody.payment_account_number()))
                        .body("total", equalTo((float) invoicePostRequestBody.total()))
                        .body("invoice_date", notNullValue())
                        .body("invoice_number", notNullValue())
                        .body("user_id", notNullValue())
                        .body("id", notNullValue())
                        .extract()
                        .as(InvoicePostResponse.class);
            });

        } catch (StepFailure sf) {
            Assertions.fail("E2E_InvoiceClient_Edge_Case : Experienced a fatal step failure and Skipped the rest of the steps: " + sf.getMessage());
        } finally {
            UserClient userClient = invoiceClient.getUserClient();
            Response response = userClient.getRefreshedToken();
            if (response.statusCode() == 200) {
                String token = response.jsonPath().getString("access_token");
                if (token != null && !token.isEmpty()) {
                    userClient.logout().then()
                            .log().all()
                            .statusCode(200)
                            .body("message", equalTo("Successfully logged out"))
                            .extract()
                            .as(UserLogoutResponse.class);
                }
            }
        }

    }
}
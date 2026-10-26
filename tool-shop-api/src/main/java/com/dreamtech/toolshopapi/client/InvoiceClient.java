package com.dreamtech.toolshopapi.client;

import com.dreamtech.toolshopapi.model.*;
import io.restassured.response.Response;
import lombok.Getter;

import java.util.List;

import static io.restassured.RestAssured.given;


@Getter
public class InvoiceClient extends BaseClient {

    public InvoiceClient(UserRegisterRequest userRegisterRequest) {
        String step = "";

        Response response = userClient.register(userRegisterRequest);
        if (response.statusCode() == 201) step = "User_Register";

        if (step.equalsIgnoreCase("User_Register")) {
            UserLoginRequest loginRequest = UserLoginRequest.builder()
                .email(userClient.getEmail())
                .password(userClient.getPassword())
                .build();

            response = userClient.login(loginRequest);
            if (response.statusCode() == 200) step = "User_Login";
        }

        if (step.equalsIgnoreCase("User_Login")) {
            response = userClient.getInfoByMe();
            if (response.statusCode() == 200) step = "User_Authorize";
        }
    }

    public Response post(InvoicePostRequest request) {
        InvoicePostRequest withRequest = InvoicePostRequest.builder()
                .user_id(request.user_id() == null ? userClient.getUserId() : request.user_id())
                .billing_address(request.billing_address())
                .billing_city(request.billing_city())
                .billing_state(request.billing_state())
                .billing_postcode(request.billing_postcode())
                .billing_country(request.billing_country())
                .payment_method(request.payment_method())
                .payment_account_name(request.payment_account_name())
                .payment_account_number(request.payment_account_number())
                .total(request.total())
                .invoicelines(
                        request.invoicelines().stream().map(item ->
                                        new InvoiceModel(item.product_id(), item.unit_price(), item.quantity())
                                ).toList())
                .build();

        Response response = given()
                .header("Authorization", "Bearer " + userClient.getBearerToken())
                .body(withRequest)
                .when()
                .post("/invoices");

        if (response.statusCode() == 201) {
            invoiceId = response.jsonPath().getInt("id");
            invoiceNumber = response.jsonPath().getString("invoice_number");

            billing_address = request.billing_address();
            billing_city = request.billing_city();
            billing_state = request.billing_state();
            billing_postcode = request.billing_postcode();
            billing_country = request.billing_country();
            payment_method = request.payment_method();
            payment_account_name = request.payment_account_name();
            payment_account_number = request.payment_account_number();
            total = request.total();
            invoicelines = request.invoicelines().stream()
                    .map(item -> new InvoiceModel(item.product_id(), item.unit_price(), item.quantity()))
                    .toList();
        }

        return response;
    }

    public Response getAll(int pageNumber) {
        return given()
                .header("Authorization", "Bearer " + userClient.getBearerToken())
                .pathParam("pageNumber", pageNumber)
                .when()
                .get("/invoices?page={pageNumber}");
    }

    public Response getInvoice() {
        Response response = given()
                .header("Authorization", "Bearer " + userClient.getBearerToken())
                .pathParam("invoiceId", invoiceId)
                .when()
                .get("/invoices/{invoiceId}");

        if (response.statusCode() == 200) {
            status = response.jsonPath().getString("status");
            status_message = response.jsonPath().getString("status_message");
        }

        return response;
    }

    public Response search(String query, int pageNumber) {
        return given()
                .header("Authorization", "Bearer " + userClient.getBearerToken())
                .pathParam("query", query)
                .pathParam("pageNumber", pageNumber)
                .when()
                .get("/invoices?q={query}&page={pageNumber}");
    }

    public Response updateStatus(InvoiceStatusUpdateRequest request) {
        Response response = given()
                .header("Authorization", "Bearer " + userClient.getBearerToken())
                .pathParam("invoiceId", invoiceId)
                .body(request)
                .when()
                .put("/invoices/{invoiceId}/status");

        if (response.statusCode() == 200) {
            status = request.status();
            status_message = request.status_message();
        }

        return response;
    }

    public Response update(InvoiceUpdateRequest request) {
        return given()
                .header("Authorization", "Bearer " + userClient.getBearerToken())
                .pathParam("invoiceId", invoiceId)
                .body(request)
                .when()
                .put("/invoices/{invoiceId}");
    }


    /******
     P R I V A T E
     ******/
    private UserClient userClient = new UserClient();
    private String billing_address = "string: billing_address";
    private String billing_city = "Chicago";
    private String billing_state = "IL";
    private String billing_postcode = "60601";
    private String billing_country = "USA";
    private String payment_method = "string: payment_method";
    private String payment_account_name = "string: payment_account_name";
    private String payment_account_number = "0123456789";
    private String status = null;
    private String status_message = null;
    private double total = 0.0d;
    private List<InvoiceModel> invoicelines = List.of(new InvoiceModel(1, 0, 0));

    private int invoiceId = -1;
    private String invoiceNumber = null;

}
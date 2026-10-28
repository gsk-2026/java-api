package com.dreamtech.toolshopapi.client;

import com.dreamtech.toolshopapi.model.*;
import io.restassured.response.Response;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;


@Getter
public class InvoiceClient extends BaseClient {

    public InvoiceClient(UserRegisterRequest userRegisterRequest) {
        Response response = userClient.register(userRegisterRequest);
        if (response.statusCode() != 201) {
            throw new IllegalArgumentException("InvoiceClient constructor failed when userClient register");
        }

        UserLoginRequest loginRequest = UserLoginRequest.builder()
            .email(userClient.getEmail())
            .password(userClient.getPassword())
            .build();
        response = userClient.login(loginRequest);
        if (response.statusCode() != 200) {
            throw new IllegalArgumentException("InvoiceClient constructor failed when userClient login");
        }
    }

    public Response post(InvoicePostRequest request) {
        return post(request, null);
    }

    public Response getAll(int pageNumber) {
        return getAll(pageNumber, null);
    }

    public Response getInvoice() {
        return getInvoice(0, null);
    }

    public Response search(String query, int pageNumber) {
        return search(query, pageNumber, null);
    }

    public Response updateStatus(InvoiceStatusUpdateRequest request) {
        return updateStatus(request, 0, null);
    }

    public Response update(InvoiceUpdateRequest request) {
        return update(request, 0, null);
    }


    /******
     *  P R O T E C T E D
     ******/

    protected Response post(InvoicePostRequest request, String token) {
        String strToken = (token == null || token.isEmpty()) ? userClient.getBearerToken() : token;

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
                        (request.invoicelines() != null) ?
                                request.invoicelines().stream().map(
                                        item ->new InvoiceModel(item.product_id(), item.unit_price(), item.quantity())
                                ).toList()
                                : Collections.emptyList()
                )
                .build();

        Response response = given()
                .header("Authorization", "Bearer " + strToken)
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

    protected Response getAll(int pageNumber, String token) {
        String strToken = (token == null || token.isEmpty()) ? userClient.getBearerToken() : token;
        return given()
                .header("Authorization", "Bearer " + strToken)
                .pathParam("pageNumber", pageNumber)
                .when()
                .get("/invoices?page={pageNumber}");
    }

    protected Response getInvoice(Integer id) {
        return getInvoice(id, null);
    }

    protected Response getInvoice(String token) {
        return getInvoice(0, token);
    }

    protected Response getInvoice(Integer id, String token) {
        int theId = (id == null || id <= 0) ? invoiceId : id;
        String strToken = (token == null || token.isEmpty()) ? userClient.getBearerToken() : token;

        Response response = given()
                .header("Authorization", "Bearer " + strToken)
                .pathParam("invoiceId", theId)
                .when()
                .get("/invoices/{invoiceId}");

        if (response.statusCode() == 200) {
            status = response.jsonPath().getString("status");
            status_message = response.jsonPath().getString("status_message");
        }

        return response;
    }

    protected Response search(String query, int pageNumber, String token) {
        String strToken = (token == null || token.isEmpty()) ? userClient.getBearerToken() : token;

        return given()
                .header("Authorization", "Bearer " + strToken)
                .pathParam("query", query)
                .pathParam("pageNumber", pageNumber)
                .when()
                .get("/invoices?q={query}&page={pageNumber}");
    }

    protected Response updateStatus(InvoiceStatusUpdateRequest request, Integer id) {
        return updateStatus(request, id, null);
    }

    protected Response updateStatus(InvoiceStatusUpdateRequest request, String token) {
        return updateStatus(request, 0, token);
    }

    protected Response updateStatus(InvoiceStatusUpdateRequest request, Integer id, String token) {
        int theId = (id == null || id <=0) ? invoiceId : id;
        String strToken = (token == null || token.isEmpty()) ? userClient.getBearerToken() : token;

        Response response = given()
                .header("Authorization", "Bearer " + strToken)
                .pathParam("invoiceId", theId)
                .body(request)
                .when()
                .put("/invoices/{invoiceId}/status");

        if (response.statusCode() == 200) {
            status = request.status();
            status_message = request.status_message();
        }

        return response;
    }


    protected Response update(InvoiceUpdateRequest request, Integer id) {
        return update(request, id, null);
    }

    protected Response update(InvoiceUpdateRequest request, String token) {
        return update(request, 0, token);
    }

    protected Response update(InvoiceUpdateRequest request, Integer id, String token) {
        int theId = (id == null || id <= 0) ? invoiceId : id;
        String strToken = (token == null || token.isEmpty()) ? userClient.getBearerToken() : token;
        return given()
                .header("Authorization", "Bearer " + strToken)
                .pathParam("invoiceId", theId)
                .body(request)
                .when()
                .put("/invoices/{invoiceId}");
    }


    /******
     *  P R I V A T E
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
package com.dreamtech.toolshopapi.client;

import com.dreamtech.toolshopapi.model.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.UUID;
import static org.hamcrest.CoreMatchers.*;


public class InvoiceClientTest {

    private InvoiceClient invoiceClient;

    @BeforeEach
    public void setUp() {
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
                .email("user.test.email." + uuid + "user.test.email." )
                .password("Welcome123!")
                .build();

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

        invoiceClient = new InvoiceClient(userRegisterRequestBody);

        Response response = invoiceClient.post(invoicePostRequestBody);
        response.then()
                //.log().all()
                .statusCode(201);
    }

    @Test
    @DisplayName("InvoiceClient : store new invoice : Success")
    public void testInvoicePostSuccess() {
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        InvoicePostRequest invoicePostRequestBody = InvoicePostRequest.builder()
                .billing_address("bill_address " + uuid)
                .billing_city("bill_city " + uuid)
                .billing_state("bill_state " + uuid)
                .billing_postcode("12345-6789")
                .billing_country("bill_country " + uuid)
                .payment_method("pay_method " + uuid)
                .payment_account_name("pay_acct_name " + uuid)
                .payment_account_number("0987654345")
                .total(123.45)
                .invoicelines(List.of(
                        InvoiceModel.builder().product_id(1).unit_price(1.11).quantity(1).build(),
                        InvoiceModel.builder().product_id(2).unit_price(2.22).quantity(2).build(),
                        InvoiceModel.builder().product_id(3).unit_price(3.33).quantity(3).build()
                ))
                .build();

        Response postResponse = invoiceClient.post(invoicePostRequestBody);

        postResponse.then()
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
    }

    @Test
    @DisplayName("InvoiceClient : retrieve specific invoice : Success")
    public void testInvoiceGetByIdSuccess() {
        Response getResponse = invoiceClient.getInvoice();

        getResponse.then()
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
    }

    @Test
    @DisplayName("InvoiceClient : retrieve all invoices : Success")
    public void testInvoiceGetAllSuccess() {
        int pageNumber = 1;
        Response getResponse = invoiceClient.getAll(pageNumber);

        getResponse.then()
                .log().all()
                .statusCode(200)
                .body("current_page", equalTo(pageNumber))
                .body("data", instanceOf(java.util.List.class));
    }

    @Test
    @DisplayName("InvoiceClient : retrieve specific invoice matching the search query : Success")
    public void testInvoiceSearchSuccess() {
        String query = "A";
        int pageNumber = 1;
        Response searchResponse = invoiceClient.search(query, pageNumber);
        searchResponse.then()
                .log().all()
                .statusCode(200)
                .body("current_page", equalTo(pageNumber))
                .body("data", instanceOf(java.util.List.class));
    }

    @Test
    @DisplayName("InvoiceClient : update specific invoice : Success")
    public void testInvoiceUpdateByIdSuccess() {
        InvoiceUpdateRequest updateRequest = InvoiceUpdateRequest.builder()
                .user_id(invoiceClient.getUserClient().getUserId())
                .billing_address("update billing_address")
                .billing_city("update billing_city")
                .billing_state("update billing_state")
                .billing_postcode("12345-6789")
                .billing_country("update billing_country")
                .payment_method("Cash on Delivery payment_method")
                .payment_account_name("Jogn Doe payment_account_name")
                .payment_account_number("0987654345")
                .total(15.54)
                .invoicelines(List.of(
                        InvoiceModel.builder().product_id(1).unit_price(1.11).quantity(1).build(),
                        InvoiceModel.builder().product_id(2).unit_price(2.22).quantity(2).build(),
                        InvoiceModel.builder().product_id(3).unit_price(3.33).quantity(3).build()
                ))
                .build();

        Response updateResponse = invoiceClient.update(updateRequest);
        updateResponse.then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(true))
                .extract()
                .as(InvoiceUpdateResponse.class);
    }


    @Test
    @DisplayName("InvoiceClient : update invoice status : Success")
    public void testInvoiceUpdateStatusSuccess() {
        InvoiceStatusUpdateRequest updateRequest = InvoiceStatusUpdateRequest.builder()
                .status("AWAITING_FULFILLMENT")
                .status_message("update invoice status")
                .build();

        Response updateResponse = invoiceClient.updateStatus(updateRequest);
        updateResponse.then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(true))
                .extract()
                .as(InvoiceStatusUpdateResponse.class);
    }

}

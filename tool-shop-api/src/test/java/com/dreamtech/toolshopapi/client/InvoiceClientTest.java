package com.dreamtech.toolshopapi.client;

import com.dreamtech.toolshopapi.model.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.empty;


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
    @DisplayName("InvoiceClient : store new invoice : Success 201")
    public void testInvoicePostSuccess201() {
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

        invoiceClient.post(invoicePostRequestBody).then()
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
    @DisplayName("InvoiceClient : store new invoice : Unauthorized 401 - invalid token")
    public void testInvoicePostUnauthorized401_invalidToken() {
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

        String token = "invalid_token";
        invoiceClient.post(invoicePostRequestBody, token).then()
                .log().all()
                .statusCode(401)
                .body("message", equalTo("Unauthorized"));
    }

    @Test
    @DisplayName("InvoiceClient : store new invoice : Unauthorized 401 - post logout")
    public void testInvoicePostUnauthorized401_postLogout() {
        invoiceClient.getUserClient().logout().then()
                .statusCode(200)
                .body("message", equalTo("Successfully logged out"));

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

        invoiceClient.post(invoicePostRequestBody).then()
                .log().all()
                .statusCode(401)
                .body("message", equalTo("Unauthorized"));
    }

    @Test
    @DisplayName("InvoiceClient : store new invoice : Unprocessable Content 422 - empty request")
    public void testInvoicePostUnprocessable422_emptyRequest() {
        InvoicePostRequest invoicePostRequestBody = InvoicePostRequest.builder().build();

        invoiceClient.post(invoicePostRequestBody).then()
                .log().all()
                .statusCode(422)
                .body("billing_address[0]", equalTo("The billing address field is required."))
                .body("billing_city[0]", equalTo("The billing city field is required."))
                .body("billing_state[0]", equalTo("The billing state field must be a string."))
                .body("billing_country[0]", equalTo("The billing country field is required."))
                .body("billing_postcode[0]", equalTo("The billing postcode field must be a string."));
    }


    @Test
    @DisplayName("InvoiceClient : retrieve specific invoice : Success 200")
    public void testInvoiceGetByIdSuccess200() {
        invoiceClient.getInvoice().then()
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
    @DisplayName("InvoiceClient : retrieve specific invoice : Success 200 - invalid Id")
    public void testInvoiceGetByIdSuccess200_invalidId() {
        Integer id = 998877;
        Response getResponse = invoiceClient.getInvoice(id);

        getResponse.then()
                .log().all()
                .statusCode(200)
                .body("$", empty());
    }

    @Test
    @DisplayName("InvoiceClient : retrieve specific invoice : Unauthorized 401 - invalid token")
    public void testInvoiceGetByIdUnauthorized401_invalidToken() {
        String token = "invalid_token";
        Response getResponse = invoiceClient.getInvoice(token);

        getResponse.then()
                .log().all()
                .statusCode(401)
                .body("message", equalTo("Unauthorized"));
    }


    @Test
    @DisplayName("InvoiceClient : retrieve all invoices : Success 200")
    public void testInvoiceGetAllSuccess200() {
        int pageNumber = new Random().nextInt(10) - 5;
        invoiceClient.getAll(pageNumber).then()
                .log().all()
                .statusCode(200)
                .body("current_page", equalTo((pageNumber>0) ? pageNumber : 1))
                //.body("from", notNullValue())
                //.body("to", notNullValue())
                .body("last_page", notNullValue())
                .body("per_page", notNullValue())
                .body("total", notNullValue())
                .body("data", instanceOf(java.util.List.class));
    }

    @Test
    @DisplayName("InvoiceClient : retrieve all invoices : Unauthorized 401 - invalid token")
    public void testInvoiceGetAllUnauthorized401_invalidToken() {
        int pageNumber = new Random().nextInt(10) - 5;
        String token = "invalid_token";
        invoiceClient.getAll(pageNumber, token).then()
                .log().all()
                .statusCode(401)
                .body("message", equalTo("Unauthorized"));
    }

    @Test
    @DisplayName("InvoiceClient : retrieve all invoices : Unauthorized 401 - post logout")
    public void testInvoiceGetAllUnauthorized401_postLogout() {
        invoiceClient.getUserClient().logout();

        int pageNumber = new Random().nextInt(10) - 5;
        invoiceClient.getAll(pageNumber).then()
                .log().all()
                .statusCode(401)
                .body("message", equalTo("Unauthorized"));
    }


    @Test
    @DisplayName("InvoiceClient : retrieve specific invoice matching the search query : Success 200")
    public void testInvoiceSearchSuccess200() {
        String query = "A";
        int pageNumber = new Random().nextInt(10) - 5;
        invoiceClient.search(query, pageNumber).then()
                .log().all()
                .statusCode(200)
                .body("current_page", equalTo((pageNumber>0) ? pageNumber : 1))
                //.body("from", notNullValue())
                //.body("to", notNullValue())
                .body("last_page", notNullValue())
                .body("per_page", notNullValue())
                .body("total", notNullValue())
                .body("data", instanceOf(java.util.List.class));
    }

    @Test
    @DisplayName("InvoiceClient : retrieve specific invoice matching the search query : Unauthorized 401 - invalid token")
    public void testInvoiceSearchUnauthorized401_invalidToken() {
        String query = "A";
        int pageNumber = new Random().nextInt(10) - 5;
        String token = "invalid_token";

        invoiceClient.search(query, pageNumber, token).then()
                .log().all()
                .statusCode(401)
                .body("message", equalTo("Unauthorized")) ;
    }


    @Test
    @DisplayName("InvoiceClient : update specific invoice : Success 200")
    public void testInvoiceUpdateByIdSuccess200() {
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

        invoiceClient.update(updateRequest).then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(true))
                .extract()
                .as(InvoiceUpdateResponse.class);
    }

    @Test
    @DisplayName("InvoiceClient : update specific invoice : Unauthorized 401 - invalid token")
    public void testInvoiceUpdateByIdUnauthorized401_invalidToken() {
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

        String token = "invalid_token";
        invoiceClient.update(updateRequest, token).then()
                .log().all()
                .statusCode(401)
                .body("message", equalTo("Unauthorized"));
    }

    @Test
    @DisplayName("InvoiceClient : update specific invoice : Not Found 404 - invalid Id")
    public void testInvoiceUpdateByIdNotFound404_invalidId() {
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

        Integer id = 999888777;
        invoiceClient.update(updateRequest, id).then()
                .log().all()
                .statusCode(404)
                .body("success", equalTo(false))
                .body("message", equalTo("Invoice not found"));
    }

    @Test
    @DisplayName("InvoiceClient : update specific invoice : Unprocessable Content 422 - empty request")
    public void testInvoiceUpdateByIdUnprocessable422_emptyRequest() {
        InvoiceUpdateRequest updateRequest = InvoiceUpdateRequest.builder().build();

        invoiceClient.update(updateRequest).then()
                .log().all()
                .statusCode(422)
                .body("user_id[0]", equalTo("The user id field is required."))
                .body("billing_address[0]", equalTo("The billing address field is required."))
                .body("billing_city[0]", equalTo("The billing city field is required."))
                .body("billing_state[0]", equalTo("The billing state field must be a string."))
                .body("billing_country[0]", equalTo("The billing country field is required."))
                .body("billing_postcode[0]", equalTo("The billing postcode field must be a string."));
    }


    @Test
    @DisplayName("InvoiceClient : update invoice status : Success 200")
    public void testInvoiceUpdateStatusSuccess200() {
        InvoiceStatusUpdateRequest updateRequest = InvoiceStatusUpdateRequest.builder()
                .status("AWAITING_FULFILLMENT")
                .status_message("update invoice status")
                .build();

        invoiceClient.updateStatus(updateRequest).then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(true))
                .extract()
                .as(InvoiceStatusUpdateResponse.class);
    }

    @Test
    @DisplayName("InvoiceClient : update invoice status : Unauthorized 401 - invalid token")
    public void testInvoiceUpdateStatusUnauthorized401_invalidToken() {
        InvoiceStatusUpdateRequest updateRequest = InvoiceStatusUpdateRequest.builder()
                .status("AWAITING_FULFILLMENT")
                .status_message("update invoice status")
                .build();
        String token = "invalid_token";

        invoiceClient.updateStatus(updateRequest, token).then()
                .log().all()
                .statusCode(401)
                .body("message", equalTo("Unauthorized"));
    }

    @Test
    @DisplayName("InvoiceClient : update invoice status : Not Found 404 - invalid Id")
    public void testInvoiceUpdateStatusNotFound401_invalidId() {
        InvoiceStatusUpdateRequest updateRequest = InvoiceStatusUpdateRequest.builder()
                .status("AWAITING_FULFILLMENT")
                .status_message("update invoice status")
                .build();
        int id = 999888777;

        invoiceClient.updateStatus(updateRequest, id).then()
                .log().all()
                .statusCode(404)
                .body("message", equalTo("Requested item not found"));
    }

    @Test
    @DisplayName("InvoiceClient : update invoice status : Unprocessable Content 422 - empty request")
    public void testInvoiceUpdateStatusUnprocessable422_emptyRequest() {
        InvoiceStatusUpdateRequest updateRequest = InvoiceStatusUpdateRequest.builder().build();

        invoiceClient.updateStatus(updateRequest).then()
                .log().all()
                .statusCode(422)
                .body("message", equalTo("The selected status is invalid."))
                .body("errors.status[0]", equalTo("The selected status is invalid."));
    }

}

package com.dreamtech.practicesoftwaretestingapi.tests.invoice;

import com.dreamtech.practicesoftwaretestingapi.model.InvoiceModel;
import com.dreamtech.practicesoftwaretestingapi.model.InvoicePostRequest;
import com.dreamtech.practicesoftwaretestingapi.model.InvoicePostResponse;
import com.dreamtech.practicesoftwaretestingapi.tests.base.InvoiceBaseTest;
import com.dreamtech.practicesoftwaretestingapi.utils.TestStatusLogger;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;


@ExtendWith(TestStatusLogger.class)
public class InvoicePostTest extends InvoiceBaseTest {

    @Test
    @DisplayName("Invoice : Post new invoice : Success 200")
    public void testInvoicePostSuccess200() {
        InvoicePostRequest requestBody = InvoicePostRequest.builder()
                .user_id(userId)
                .billing_address("string billing_address")
                .billing_city("string billing_city")
                .billing_state("string billing_state")
                .billing_postcode("12345-6789")
                .billing_country("string billing_country")
                .payment_method("Cash on Delivery payment_method")
                .payment_account_name("Jogn Doe payment_account_name")
                .payment_account_number("0987654345")
                .total(123.45)
                .invoicelines(List.of(
                        InvoiceModel.builder().product_id(1).unit_price(1.11).quantity(1).build(),
                        InvoiceModel.builder().product_id(2).unit_price(2.22).quantity(2).build(),
                        InvoiceModel.builder().product_id(3).unit_price(3.33).quantity(3).build()
                ))
                .build();

        Response postResponse = given()
                .header("Authorization", "Bearer " + bearerToken)
                .body(requestBody)
                .when()
                .post("/invoices");

        postResponse.then()
                .log().all()
                .statusCode(201)
                .body("user_id", equalTo(requestBody.user_id()))
                .body("billing_address", equalTo(requestBody.billing_address()))
                .body("billing_city", equalTo(requestBody.billing_city()))
                .body("billing_state", equalTo(requestBody.billing_state()))
                .body("billing_postcode", equalTo(requestBody.billing_postcode()))
                .body("billing_country", equalTo(requestBody.billing_country()))
                .body("payment_method", equalTo(requestBody.payment_method()))
                .body("payment_account_name", equalTo(requestBody.payment_account_name()))
                .body("payment_account_number", equalTo(requestBody.payment_account_number()))
                .body("total", equalTo((float)requestBody.total()))
                .body("invoice_date", notNullValue(String.class))
                .body("invoice_number", notNullValue(String.class))
                .body("id", notNullValue(Integer.class))
                .extract()
                .as(InvoicePostResponse.class);

        userId = postResponse.jsonPath().getInt("user_id");
        invoiceId = postResponse.jsonPath().getInt("id");
    }

    @Test
    @DisplayName("Invoice : Post new invoice : Success 201 - Optional Field Missing")
    public void testInvoicePostSuccess201_OptionalFieldMissing() {
        InvoicePostRequest requestBody = InvoicePostRequest.builder()
                .user_id(userId)
                .billing_address("string billing_address")
                .billing_city("string billing_city")
                .billing_state("string billing_state")
                .billing_postcode("12345-6789")
                .billing_country("string billing_country")
                //.payment_method("Cash on Delivery payment_method")
                //.payment_account_name("Jogn Doe payment_account_name")
                //.payment_account_number("0987654345")
                //.total(123.45)
                .invoicelines(List.of(
                        InvoiceModel.builder().product_id(1).unit_price(1.11).quantity(1).build(),
                        InvoiceModel.builder().product_id(2).unit_price(2.22).quantity(2).build(),
                        InvoiceModel.builder().product_id(3).unit_price(3.33).quantity(3).build()
                ))
                .build();

        given()
                .header("Authorization", "Bearer " + bearerToken)
                .body(requestBody)
                .when()
                .post("/invoices")
                .then()
                .log().all()
                .statusCode(201)
                .body("user_id", equalTo(requestBody.user_id()))
                .body("billing_address", equalTo(requestBody.billing_address()))
                .body("billing_city", equalTo(requestBody.billing_city()))
                .body("billing_state", equalTo(requestBody.billing_state()))
                .body("billing_postcode", equalTo(requestBody.billing_postcode()))
                .body("billing_country", equalTo(requestBody.billing_country()))
                .body("payment_method", nullValue())
                .body("payment_account_name", nullValue())
                .body("payment_account_number", nullValue())
                .body("total", equalTo(0))
                .body("invoice_date", notNullValue(String.class))
                .body("invoice_number", notNullValue(String.class))
                .body("id", notNullValue(Integer.class));;
    }

    @Test
    @DisplayName("Invoice : Post new invoice : Unauthorized 401 - Invalid Token")
    public void testInvoicePostUnauthorized401_InvalidToken() {
        InvoicePostRequest requestBody = InvoicePostRequest.builder()
                .user_id(userId)
                .billing_address("string billing_address")
                .billing_city("string billing_city")
                .billing_state("string billing_state")
                .billing_postcode("12345-6789")
                .billing_country("string billing_country")
                .payment_method("Cash on Delivery payment_method")
                .payment_account_name("Jogn Doe payment_account_name")
                .payment_account_number("0987654345")
                .total(123.45)
                .invoicelines(List.of(
                        InvoiceModel.builder().product_id(1).unit_price(1.11).quantity(1).build(),
                        InvoiceModel.builder().product_id(2).unit_price(2.22).quantity(2).build(),
                        InvoiceModel.builder().product_id(3).unit_price(3.33).quantity(3).build()
                ))
                .build();

        given()
                .header("Authorization", "Bearer ")
                .body(requestBody)
                .when()
                .post("/invoices")
                .then()
                .log().all()
                .statusCode(401)
                .body("message", equalTo("Unauthorized"));
    }

    @Test
    @DisplayName("Invoice : Post new invoice : Not Found 404 - Invalid Endpoint")
    public void testInvoicePostNotFound404_InvalidEndpoint() {
        InvoicePostRequest requestBody = InvoicePostRequest.builder()
                .user_id(userId)
                .billing_address("string billing_address")
                .billing_city("string billing_city")
                .billing_state("string billing_state")
                .billing_postcode("12345-6789")
                .billing_country("string billing_country")
                .payment_method("Cash on Delivery payment_method")
                .payment_account_name("Jogn Doe payment_account_name")
                .payment_account_number("0987654345")
                .total(123.45)
                .invoicelines(List.of(
                        InvoiceModel.builder().product_id(1).unit_price(1.11).quantity(1).build(),
                        InvoiceModel.builder().product_id(2).unit_price(2.22).quantity(2).build(),
                        InvoiceModel.builder().product_id(3).unit_price(3.33).quantity(3).build()
                ))
                .build();

        given()
                .header("Authorization", "Bearer " + bearerToken)
                .body(requestBody)
                .when()
                .post("/invoice")
                .then()
                .log().all()
                .statusCode(404)
                .body("message", equalTo("Resource not found")) ;
    }

    @Test
    @DisplayName("Invoice : Post new invoice : Method Not Allowed 405 - Patch")
    public void testInvoicePostMethodNotAllowed405_Patch() {
        InvoicePostRequest requestBody = InvoicePostRequest.builder()
                .user_id(userId)
                .billing_address("string billing_address")
                .billing_city("string billing_city")
                .billing_state("string billing_state")
                .billing_postcode("12345-6789")
                .billing_country("string billing_country")
                .payment_method("Cash on Delivery payment_method")
                .payment_account_name("Jogn Doe payment_account_name")
                .payment_account_number("0987654345")
                .total(123.45)
                .invoicelines(List.of(
                        InvoiceModel.builder().product_id(1).unit_price(1.11).quantity(1).build(),
                        InvoiceModel.builder().product_id(2).unit_price(2.22).quantity(2).build(),
                        InvoiceModel.builder().product_id(3).unit_price(3.33).quantity(3).build()
                ))
                .build();

        given()
                .header("Authorization", "Bearer " + bearerToken)
                .body(requestBody)
                .when()
                .patch("/invoices")
                .then()
                .log().all()
                .statusCode(405)
                .body("message", equalTo("Method is not allowed for the requested route"));
    }

    @Test
    @DisplayName("Invoice : Post new invoice : Unprocessable Content 422 - Invalid UserId")
    public void testInvoicePostUnprocessable422_InvalidUserId() {
        InvoicePostRequest requestBody = InvoicePostRequest.builder()
                .user_id(999999)
                .billing_address("string billing_address")
                .billing_city("string billing_city")
                .billing_state("string billing_state")
                .billing_postcode("12345-6789")
                .billing_country("string billing_country")
                .payment_method("Cash on Delivery payment_method")
                .payment_account_name("Jogn Doe payment_account_name")
                .payment_account_number("0987654345")
                .total(123.45)
                .invoicelines(List.of(
                        InvoiceModel.builder().product_id(1).unit_price(1.11).quantity(1).build(),
                        InvoiceModel.builder().product_id(2).unit_price(2.22).quantity(2).build(),
                        InvoiceModel.builder().product_id(3).unit_price(3.33).quantity(3).build()
                ))
                .build();

        Response postResponse = given()
                .header("Authorization", "Bearer " + bearerToken)
                .body(requestBody)
                .when()
                .post("/invoices");

        postResponse.then()
                .log().all()
                .statusCode(422)
                .body("user_id[0]", equalTo("The selected user id is invalid."));
    }

    @Test
    @DisplayName("Invoice : Post new invoice : Unprocessable Content 422 - Required Field Missing")
    public void testInvoicePostUnprocessable422_RequiredFieldMissing() {
        InvoicePostRequest requestBody = InvoicePostRequest.builder()
                .user_id(userId)
                //.billing_address("string billing_address")
                //.billing_city("string billing_city")
                //.billing_state("string billing_state")
                //.billing_postcode("12345-6789")
                //.billing_country("string billing_country")
                .payment_method("Cash on Delivery payment_method")
                .payment_account_name("Jogn Doe payment_account_name")
                .payment_account_number("0987654345")
                .total(123.45)
                .invoicelines(List.of(
                        InvoiceModel.builder().product_id(1).unit_price(1.11).quantity(1).build(),
                        InvoiceModel.builder().product_id(2).unit_price(2.22).quantity(2).build(),
                        InvoiceModel.builder().product_id(3).unit_price(3.33).quantity(3).build()
                ))
                .build();

        Response postResponse = given()
                .header("Authorization", "Bearer " + bearerToken)
                .body(requestBody)
                .when()
                .post("/invoices");

        postResponse.then()
                .log().all()
                .statusCode(422)
                .body("billing_address[0]", equalTo("The billing address field is required."))
                .body("billing_city[0]", equalTo("The billing city field is required."))
                .body("billing_state[0]", equalTo("The billing state field must be a string."))
                .body("billing_postcode[0]", equalTo("The billing postcode field must be a string."))
                .body("billing_country[0]", equalTo("The billing country field is required."));
    }

    @Test
    @DisplayName("Invoice : Post new invoice : Server Error 500 - Invoiceline Missing")
    public void testInvoicePostServerError500_InvoicelineMissing() {
        InvoicePostRequest requestBody = InvoicePostRequest.builder()
                .user_id(userId)
                .billing_address("string billing_address")
                .billing_city("string billing_city")
                .billing_state("string billing_state")
                .billing_postcode("12345-6789")
                .billing_country("string billing_country")
                .payment_method("Cash on Delivery payment_method")
                .payment_account_name("Jogn Doe payment_account_name")
                .payment_account_number("0987654345")
                .total(123.45)
                /*.invoicelines(List.of(
                        InvoiceModel.builder().product_id(1).unit_price(1.11).quantity(1).build(),
                        InvoiceModel.builder().product_id(2).unit_price(2.22).quantity(2).build(),
                        InvoiceModel.builder().product_id(3).unit_price(3.33).quantity(3).build()
                ))*/
                .build();

        Response postResponse = given()
                .header("Authorization", "Bearer " + bearerToken)
                .body(requestBody)
                .when()
                .post("/invoices");

        postResponse.then()
                .log().all()
                .statusCode(500)
                .body("message", equalTo("Server Error"));
    }

}

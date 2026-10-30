package com.dreamtech.practicesoftwaretestingapi.tests.invoice;

import com.dreamtech.practicesoftwaretestingapi.model.InvoiceModel;
import com.dreamtech.practicesoftwaretestingapi.model.InvoiceUpdateRequest;
import com.dreamtech.practicesoftwaretestingapi.model.InvoiceUpdateResponse;
import com.dreamtech.practicesoftwaretestingapi.tests.base.InvoiceBaseTest;
import com.dreamtech.practicesoftwaretestingapi.utils.TestStatusLogger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;


@ExtendWith(TestStatusLogger.class)
public class InvoiceUpdateByIdTest extends InvoiceBaseTest {

    @Test
    @DisplayName("Invoice : Update invoice by Id : Success 200")
    public void testInvoiceUpdateByIdSuccess200() {
        InvoiceUpdateRequest updateRequestBody = InvoiceUpdateRequest.builder()
                .user_id(userId)
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

        given()
                .header("Authorization", "Bearer " + bearerToken)
                .pathParam("invoiceId", invoiceId)
                .body(updateRequestBody)
                .when()
                .put("/invoices/{invoiceId}")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(true))
                .extract()
                .as(InvoiceUpdateResponse.class);
    }

    @Test
    @DisplayName("Invoice : Update invoice by Id : Success 200 - Optional Field Missing")
    public void testInvoiceUpdateByIdSuccess200_OptionalFieldMissing() {
        String prefix = UUID.randomUUID().toString().substring(0, 8) + "-";

        InvoiceUpdateRequest updateRequestBody = InvoiceUpdateRequest.builder()
                .user_id(userId)
                .billing_address("update billing_address")
                .billing_city("update billing_city")
                .billing_state("update billing_state")
                .billing_postcode("12345-6789")
                .billing_country("update billing_country")
                //.payment_method("Cash on Delivery payment_method")
                //.payment_account_name("Jogn Doe payment_account_name")
                //.payment_account_number("0987654345")
                //.total(15.54)
                .invoicelines(List.of(
                        InvoiceModel.builder().product_id(1).unit_price(1.11).quantity(1).build(),
                        InvoiceModel.builder().product_id(2).unit_price(2.22).quantity(2).build(),
                        InvoiceModel.builder().product_id(3).unit_price(3.33).quantity(3).build()
                ))
                .build();

        given()
                .header("Authorization", "Bearer " + bearerToken)
                .pathParam("invoiceId", invoiceId)
                .body(updateRequestBody)
                .when()
                .put("/invoices/{invoiceId}")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Invoice : Update invoice by Id : Unauthorized 401- Invalid Token")
    public void testInvoiceUpdateByIdUnauthorized401_InvalidToken() {
        InvoiceUpdateRequest updateRequestBody = InvoiceUpdateRequest.builder()
                .user_id(userId)
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

        given()
                .header("Authorization", "Bearer ABCDEFGHIJKLMNOPQRSTUVWXYZ")
                .pathParam("invoiceId", invoiceId)
                .body(updateRequestBody)
                .when()
                .put("/invoices/{invoiceId}")
                .then()
                .log().all()
                .statusCode(401)
                .body("message", equalTo("Unauthorized"));
    }

    @Test
    @DisplayName("Invoice : Update invoice by Id : Not Found 404 - Invalid Endpoint")
    public void testInvoiceUpdateByIdNotFound404_InvalidEndpoint() {
        InvoiceUpdateRequest updateRequestBody = InvoiceUpdateRequest.builder()
                .user_id(userId)
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

        given()
                .header("Authorization", "Bearer " + bearerToken)
                .pathParam("invoiceId", invoiceId)
                .body(updateRequestBody)
                .when()
                .put("/invoice/{invoiceId}")
                .then()
                .log().all()
                .statusCode(404)
                .body("message", equalTo("Resource not found"));
    }

    @Test
    @DisplayName("Invoice : Update invoice by Id : Method Not Allowed 405 - Patch")
    public void testInvoiceUpdateByIdMethodNotAllowed405_Patch() {
        InvoiceUpdateRequest updateRequestBody = InvoiceUpdateRequest.builder()
                .user_id(userId)
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

        given()
                .header("Authorization", "Bearer " + bearerToken)
                .pathParam("invoiceId", invoiceId)
                .body(updateRequestBody)
                .when()
                .patch("/invoices/{invoiceId}")
                .then()
                .log().all()
                .statusCode(405)
                .body("message", equalTo("Method is not allowed for the requested route"));
    }

    @Test
    @DisplayName("Invoice : Update invoice by Id : Unprocessable Content 422 - Required Field Missing")
    public void testInvoiceUpdateByIdUnprocessable422_RequiredFieldMissing() {
        InvoiceUpdateRequest updateRequestBody = InvoiceUpdateRequest.builder()
                //.user_id(userId)
                //.billing_address("update billing_address")
                //.billing_city("update billing_city")
                //.billing_state("update billing_state")
                //.billing_postcode("12345-6789")
                //.billing_country("update billing_country")
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

        given()
                .header("Authorization", "Bearer " + bearerToken)
                .pathParam("invoiceId", invoiceId)
                .body(updateRequestBody)
                .when()
                .put("/invoices/{invoiceId}")
                .then()
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
    @DisplayName("Invoice : Update invoice by Id : Internal Server Error 500 - Invoiceline Missing")
    public void testInvoiceUpdateByIdServerError500_InvoicelineMissing() {
        InvoiceUpdateRequest updateRequestBody = InvoiceUpdateRequest.builder()
                .user_id(userId)
                .billing_address("update billing_address")
                .billing_city("update billing_city")
                .billing_state("update billing_state")
                .billing_postcode("12345-6789")
                .billing_country("update billing_country")
                .payment_method("Cash on Delivery payment_method")
                .payment_account_name("Jogn Doe payment_account_name")
                .payment_account_number("0987654345")
                .total(15.54)
                /*.invoicelines(List.of(
                        InvoiceModel.builder().product_id(1).unit_price(1.11).quantity(1).build(),
                        InvoiceModel.builder().product_id(2).unit_price(2.22).quantity(2).build(),
                        InvoiceModel.builder().product_id(3).unit_price(3.33).quantity(3).build()
                ))*/
                .build();

        given()
                .header("Authorization", "Bearer " + bearerToken)
                .pathParam("invoiceId", invoiceId)
                .body(updateRequestBody)
                .when()
                .put("/invoices/{invoiceId}")
                .then()
                .log().all()
                .statusCode(500)
                .body("message", equalTo("Server Error"));
    }

}

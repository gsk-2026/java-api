package com.dreamtech.practicesoftwaretestingapi.tests.invoice;

import com.dreamtech.practicesoftwaretestingapi.model.InvoiceGetResponse;
import com.dreamtech.practicesoftwaretestingapi.tests.base.InvoiceBaseTest;
import com.dreamtech.practicesoftwaretestingapi.utils.TestStatusLogger;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.empty;


@ExtendWith(TestStatusLogger.class)
public class InvoiceGetByIdTest extends InvoiceBaseTest {

    @Test
    @DisplayName("Invoice : Get invoice by Id : Success 200")
    public void testInvoiceGetByIdSuccess200() {
        Response getResponse = given()
                .header("Authorization", "Bearer " + bearerToken)
                .pathParam("invoiceId", invoiceId)
                .when()
                .get("/invoices/{invoiceId}");

        getResponse.then()
                .log().all()
                .statusCode(200)
                .body("id", equalTo(invoiceId))
                .body("user_id", equalTo(userId))
                .body("invoice_number", notNullValue())
                .body("billing_address", notNullValue())
                .body("billing_city", notNullValue())
                .body("billing_state", notNullValue())
                .body("billing_country", notNullValue())
                .body("billing_postcode", notNullValue())
                .body("total", notNullValue())
                .body("payment_method", notNullValue())
                .body("payment_account_name", notNullValue())
                .body("payment_account_number", notNullValue())
                .body("status", notNullValue())
                .body("invoicelines", instanceOf(java.util.List.class))
                .extract()
                .as(InvoiceGetResponse.class);

        userId = getResponse.jsonPath().getInt("user_id");
        invoiceId = getResponse.jsonPath().getInt("id");
    }

    @Test
    @DisplayName("Invoice : Get invoice by Id : Success 200 - Invalid InvoiceId")
    public void testInvoiceGetByIdSuccess200_InvalidInvoiceId() {
        Response getResponse = given()
                .header("Authorization", "Bearer " + bearerToken)
                .pathParam("invoiceId", 999999)
                .when()
                .get("/invoices/{invoiceId}");

        getResponse.then()
                .log().all()
                .statusCode(200)
                .body("$", empty());
    }

    @Test
    @DisplayName("Invoice : Get invoice by Id : Unauthorized 401 -Invalid Token")
    public void testInvoiceGetByIdUnauthorized401_InvalidToken() {
        Response getResponse = given()
                .header("Authorization", "Bearer ")
                .pathParam("invoiceId", userId)
                .when()
                .get("/invoices/{invoiceId}");

        getResponse.then()
                .log().all()
                .statusCode(401)
                .body("message", Matchers.equalTo("Unauthorized"));
    }

    @Test
    @DisplayName("Invoice : Get invoice by Id : Not Found 404 - Invalid Endpoint")
    public void testInvoiceGetByIdNotFound404_InvalidEndpoint() {
        Response getResponse = given()
                .header("Authorization", "Bearer " + bearerToken)
                .pathParam("invoiceId", invoiceId)
                .when()
                .get("/invoice/{invoiceId}");

        getResponse.then()
                .log().all()
                .statusCode(404)
                .body("message", equalTo("Resource not found"));
    }
    @Test
    @DisplayName("Invoice : Get invoice by Id : Method Not Allowed 405 - Patch")
    public void testInvoiceGetByIdMethodNotAllowed405_Patch() {
        Response getResponse = given()
                .header("Authorization", "Bearer " + bearerToken)
                .pathParam("invoiceId", invoiceId)
                .when()
                .patch("/invoices/{invoiceId}");

        getResponse.then()
                .log().all()
                .statusCode(405)
                .body("message", equalTo("Method is not allowed for the requested route"));
    }

}

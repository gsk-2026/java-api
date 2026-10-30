package com.dreamtech.practicesoftwaretestingapi.tests.invoice;

import com.dreamtech.practicesoftwaretestingapi.model.InvoiceStatusUpdateRequest;
import com.dreamtech.practicesoftwaretestingapi.model.InvoiceStatusUpdateResponse;
import com.dreamtech.practicesoftwaretestingapi.tests.base.InvoiceBaseTest;
import com.dreamtech.practicesoftwaretestingapi.utils.TestStatusLogger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;


@ExtendWith(TestStatusLogger.class)
public class InvoiceStatusUpdateByIdTest extends InvoiceBaseTest {

    @Test
    @DisplayName("Invoice : Update invoice status by Id :Success 200")
    public void testInvoiceStatusUpdateByIdSuccess200() {
        InvoiceStatusUpdateRequest updateRequestBody = InvoiceStatusUpdateRequest.builder()
                .status("AWAITING_FULFILLMENT")
                .status_message("update invoice status")
                .build();

        given()
                .header("Authorization", "Bearer " + bearerToken)
                .pathParam("invoiceId", invoiceId)
                .body(updateRequestBody)
                .when()
                .put("/invoices/{invoiceId}/status")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(true))
                .extract()
                .as(InvoiceStatusUpdateResponse.class);
    }

    @Test
    @DisplayName("Invoice : Update invoice status by Id : Unauthorized 401 - Invalid Token")
    public void testInvoiceStatusUpdateByIdUnauthorized401_InvalidToken() {
        InvoiceStatusUpdateRequest updateRequestBody = InvoiceStatusUpdateRequest.builder()
                .status("AWAITING_FULFILLMENT")
                .status_message("update invoice status")
                .build();

        given()
                .header("Authorization", "Bearer ")
                .pathParam("invoiceId", invoiceId)
                .body(updateRequestBody)
                .when()
                .put("/invoices/{invoiceId}/status")
                .then()
                .log().all()
                .statusCode(401)
                .body("message", equalTo("Unauthorized"));
    }

    @Test
    @DisplayName("Invoice : Update invoice status by Id : Not Found 404 - Invalid InvoiceId")
    public void testInvoiceStatusUpdateByIdNotFound404_InvalidInvoiceId() {
        InvoiceStatusUpdateRequest updateRequestBody = InvoiceStatusUpdateRequest.builder()
                .status("AWAITING_FULFILLMENT")
                .status_message("update invoice status")
                .build();

        given()
                .header("Authorization", "Bearer " + bearerToken)
                .pathParam("invoiceId", 999999)
                .body(updateRequestBody)
                .when()
                .put("/invoices/{invoiceId}/status")
                .then()
                .log().all()
                .statusCode(404)
                .body("message", equalTo("Requested item not found"));
    }

    @Test
    @DisplayName("Invoice : Update invoice status by Id : Method Not Allowed 405 - Patch")
    public void testInvoiceStatusUpdateByIdMethodNotAllowed405_Patch() {
        InvoiceStatusUpdateRequest updateRequestBody = InvoiceStatusUpdateRequest.builder()
                .status("AWAITING_FULFILLMENT")
                .status_message("update invoice status")
                .build();

        given()
                .header("Authorization", "Bearer " + bearerToken)
                .pathParam("invoiceId", invoiceId)
                .body(updateRequestBody)
                .when()
                .patch("/invoices/{invoiceId}/status")
                .then()
                .log().all()
                .statusCode(405)
                .body("message", equalTo("Method is not allowed for the requested route"));
    }

    @Test
    @DisplayName("Invoice : Update invoice status by Id : Unprocessable Content 422 - Invalid Request")
    public void testInvoiceStatusUpdateByIdUnprocessable422_InvalidRequest() {
        InvoiceStatusUpdateRequest updateRequestBody = InvoiceStatusUpdateRequest.builder().build();

        given()
                .header("Authorization", "Bearer " + bearerToken)
                .pathParam("invoiceId", invoiceId)
                .body(updateRequestBody)
                .when()
                .put("/invoices/{invoiceId}/status")
                .then()
                .log().all()
                .statusCode(422)
                .body("message", equalTo("The selected status is invalid."))
                .body("errors.status[0]", equalTo("The selected status is invalid."));
    }

}

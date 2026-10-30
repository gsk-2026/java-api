package com.dreamtech.practicesoftwaretestingapi.tests.invoice;

import com.dreamtech.practicesoftwaretestingapi.tests.base.InvoiceBaseTest;
import com.dreamtech.practicesoftwaretestingapi.utils.TestStatusLogger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;


@ExtendWith(TestStatusLogger.class)
public class InvoiceGetAllTest extends InvoiceBaseTest {

    @Test
    @DisplayName("Invoice - Get all invoices : Success 200")
    public void testInvoiceGetAllSuccess200() {
        int pageNumber = new Random().nextInt(10) - 5;

        given()
                .header("Authorization", "Bearer " + bearerToken)
                .pathParam("pageNumber", pageNumber)
                .when()
                .get("/invoices?page={pageNumber}")
                .then()
                .log().all()
                .statusCode(200)
                .body("current_page", equalTo(pageNumber > 0 ? pageNumber : 1))
                //.body("from", notNullValue())
                //.body("to", notNullValue())
                .body("last_page", notNullValue())
                .body("per_page", notNullValue())
                .body("total", notNullValue())
                .body("data", instanceOf(List.class));
    }

    @Test
    @DisplayName("Invoice - Get all invoices : Unauthorized 401 -Invalid Token")
    public void testInvoiceGetAllUnauthorized401_InvalidToken() {
        int pageNumber = new Random().nextInt(10) - 5;

        given()
                .header("Authorization", "Bearer 1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ")
                .pathParam("pageNumber", pageNumber)
                .when()
                .get("/invoices?page={pageNumber}")
                .then()
                .log().all()
                .statusCode(401)
                .body("message", equalTo("Unauthorized"));
    }

    @Test
    @DisplayName("Invoice - Get all invoices : Not Found 404 - Invalid Endpoint")
    public void testInvoiceGetAllNotFound404_InvalidEndpoint() {
        int pageNumber = new Random().nextInt(10) - 5;

        given()
                .header("Authorization", "Bearer " + bearerToken)
                .pathParam("pageNumber", pageNumber)
                .when()
                .get("/invoic?page={pageNumber}")
                .then()
                .log().all()
                .statusCode(404)
                .body("message", equalTo("Resource not found"));
    }

    @Test
    @DisplayName("Invoice - Get all invoices : Method Not Allowed 405")
    public void testInvoiceGetAllMethodNotAllowed405_Patch() {
        int pageNumber = new Random().nextInt(10) - 5;

        given()
                .header("Authorization", "Bearer 1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ")
                .pathParam("pageNumber", pageNumber)
                .when()
                .patch("/invoices?page={pageNumber}")
                .then()
                .log().all()
                .statusCode(405)
                .body("message", equalTo("Method is not allowed for the requested route"));
    }

}

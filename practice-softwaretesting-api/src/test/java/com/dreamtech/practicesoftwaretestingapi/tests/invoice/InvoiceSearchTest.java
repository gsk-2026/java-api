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
import static org.hamcrest.CoreMatchers.notNullValue;


@ExtendWith(TestStatusLogger.class)
public class InvoiceSearchTest extends InvoiceBaseTest {

    @Test
    @DisplayName("Invoice : Get invoices matching the search query : Success 200")
    public void testInvoiceSearchSuccess200() {
        String query = "invoice_number";
        int pageNumber = new Random().nextInt(10) - 5;

        given()
                .header("Authorization", "Bearer " + bearerToken)
                .pathParam("query", query)
                .pathParam("pageNumber", pageNumber)
                .when()
                .get("/invoices?q={query}&page={pageNumber}")
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
    @DisplayName("Invoice : Get invoices matching the search query : Success 200 - Empty query")
    public void testInvoiceSearchSuccess200_EmptyQuery() {
        String query = "";
        int pageNumber = new Random().nextInt(10) - 5;

        given()
                .header("Authorization", "Bearer " + bearerToken)
                .pathParam("query", query)
                .pathParam("pageNumber", pageNumber)
                .when()
                .get("/invoices?q={query}&page={pageNumber}")
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
    @DisplayName("Invoice : Get invoices matching the search query : Unauthorized 401 - UnauthorizedInvalid Token")
    public void testInvoiceSearchUnauthorized401_InvalidToken() {
        String query = "status";
        int pageNumber = new Random().nextInt(10) - 5;

        given()
                .header("Authorization", "Bearer ")
                .pathParam("query", query)
                .pathParam("pageNumber", pageNumber)
                .when()
                .get("/invoices?q={query}&page={pageNumber}")
                .then()
                .log().all()
                .statusCode(401)
                .body("message", equalTo("Unauthorized"));
    }

    @Test
    @DisplayName("Invoice : Get invoices matching the search query : Not Found 404 - Invalid Endpoint")
    public void testInvoiceSearcNotFoundSuccess404_InvalidEndpoint() {
        String query = "invoice_number";
        int pageNumber = new Random().nextInt(10) - 5;

        given()
                .header("Authorization", "Bearer " + bearerToken)
                .pathParam("query", query)
                .pathParam("pageNumber", pageNumber)
                .when()
                .get("/invoice?q={query}&page={pageNumber}")
                .then()
                .log().all()
                .statusCode(404)
                .body("message", equalTo("Resource not found"));
    }


    @Test
    @DisplayName("Invoice : Get invoices matching the search query : Method Not Allowed 405 - Patch")
    public void testInvoiceSearchMethodNotAllowed405_Patch() {
        String query = "invoice_number";
        int pageNumber = new Random().nextInt(10) - 5;

        given()
                .header("Authorization", "Bearer " + bearerToken)
                .pathParam("query", query)
                .pathParam("pageNumber", pageNumber)
                .when()
                .patch("/invoices?q={query}&page={pageNumber}")
                .then()
                .log().all()
                .statusCode(405)
                .body("message", equalTo("Method is not allowed for the requested route"));
    }

}

package com.dreamtech.practicesoftwaretestingapi.tests.base;

import com.dreamtech.practicesoftwaretestingapi.model.InvoiceModel;
import com.dreamtech.practicesoftwaretestingapi.model.InvoicePostRequest;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;


public class InvoiceBaseTest extends UserBaseTest {

    protected static Integer invoiceId;

    @BeforeAll
    public static void setup() {
        // Store an invoice
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
                .total(15.54)
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
                //.log().all()
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
                .body("id", notNullValue(Integer.class));

        invoiceId = postResponse.jsonPath().getInt("id");
    }

}
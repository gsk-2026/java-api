package com.dreamtech.practicesoftwaretestingapi.tests.base;

import com.dreamtech.practicesoftwaretestingapi.config.ConfigManager;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;


public class ApiBaseTest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = ConfigManager.get("API_BASE_URL");

        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();
    }

}

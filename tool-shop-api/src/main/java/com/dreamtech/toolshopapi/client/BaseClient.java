package com.dreamtech.toolshopapi.client;

import com.dreamtech.toolshopapi.config.ConfigManager;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;

public class BaseClient {

    static {
        RestAssured.baseURI = ConfigManager.get("API_BASE_URL");

        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();
    }

}
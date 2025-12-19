package org.player.controller.qa.http;


import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.player.controller.qa.config.ApplicationProperties;

public class BaseHttpClient {

    protected final static ApplicationProperties PROPERTIES = new ApplicationProperties();

    protected RequestSpecification getHttpClient() {

        return RestAssured.given()
                .baseUri(PROPERTIES.getProperty("base.url"))
                .log().all()
                .contentType("application/json");
    }

}

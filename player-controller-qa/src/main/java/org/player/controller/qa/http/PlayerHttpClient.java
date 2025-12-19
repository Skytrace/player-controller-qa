package org.player.controller.qa.http;

import io.restassured.response.Response;

public class PlayerHttpClient extends BaseHttpClient {

    public Response getPlayers() {
        return getHttpClient()
                .get("/player/get/all");
    }

}

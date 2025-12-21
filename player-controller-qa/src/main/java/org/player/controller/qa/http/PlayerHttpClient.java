package org.player.controller.qa.http;

import io.restassured.response.Response;
import org.player.controller.qa.dto.CreatePlayerRequestDto;
import org.player.controller.qa.dto.PlayerUpdateRequestDto;

import java.util.Map;

public class PlayerHttpClient extends BaseHttpClient {

    public Response getPlayers() {
        return getHttpClient()
                .get("/player/get/all");
    }

    public Response createPlayer(CreatePlayerRequestDto user, String editor) {
        return getHttpClient()
                .queryParam("age", 25)
                .queryParam("gender", user.getGender())
                .queryParam("login", user.getLogin())
                .queryParam("password", user.getPassword())
                .queryParam("role", user.getRole())
                .queryParam("screenName", user.getScreenName())
                .get("/player/create/" + editor);
    }

    public Response updatePlayer(Map<String, Object> user, String editor, Long userId) {
        return getHttpClient()
                .body(user)
                .patch("/player/update/" + editor + "/" + userId);
    }

    public Response deletePlayer(Object id, String editor) {
        String body = String.format("{\n" +
                "  \"playerId\": \"%s\"\n" +
                "}", String.valueOf(id));

        return getHttpClient()
                .body(body)
                .delete("/player/delete/" + editor);
    }

}

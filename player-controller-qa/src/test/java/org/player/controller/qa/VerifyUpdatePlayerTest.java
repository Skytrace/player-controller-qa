package org.player.controller.qa;

import io.restassured.response.Response;
import org.player.controller.qa.base.BaseTest;
import org.player.controller.qa.dto.CreatePlayerRequestDto;
import org.player.controller.qa.dto.CreatePlayerResponseDto;
import org.player.controller.qa.dto.PlayerUpdateResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.*;

public class VerifyUpdatePlayerTest extends BaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(VerifyUpdatePlayerTest.class);
    private Long testPlayerId;

    @Test(groups = "updatePlayer")
    public void updatePlayerAgeOnlyOneField() {
        // create test player
        CreatePlayerRequestDto expectedTestPlayer = createValidPlayerHelper("user");
        Response createPlayerResponse = httpClient.createPlayer(expectedTestPlayer, "supervisor");

        assertEquals(createPlayerResponse.getStatusCode(), 200, "Status code mismatch");

        CreatePlayerResponseDto actualPlayer = createPlayerResponse.as(CreatePlayerResponseDto.class);
        testPlayerId = actualPlayer.getId();

        // update player
        Map<String, Object> playerUpdateRequest = new HashMap<>();
        playerUpdateRequest.put("age", Long.valueOf(18L));
        // request update player
        Response actualUpdatePlayerResponse = httpClient.updatePlayer(playerUpdateRequest, "supervisor", testPlayerId);
        assertEquals(actualUpdatePlayerResponse.getStatusCode(), 200);

        PlayerUpdateResponseDto actualUpdatedPlayer = actualUpdatePlayerResponse.as(PlayerUpdateResponseDto.class);

        SoftAssert assertion = new SoftAssert();
        assertion.assertEquals(actualUpdatedPlayer.getAge(), Integer.valueOf(18),
                "Actual age is not as expected");
        assertion.assertEquals(actualUpdatedPlayer.getGender(), expectedTestPlayer.getGender(),
                "Actual gender is not as expected");
        assertion.assertEquals(actualUpdatedPlayer.getLogin(), expectedTestPlayer.getLogin(),
                "actual login is not as expected");
        assertion.assertEquals(actualUpdatedPlayer.getScreenName(), expectedTestPlayer.getScreenName(),
                "actual screen name is not as expected");
        assertion.assertEquals(actualUpdatedPlayer.getRole(), expectedTestPlayer.getRole(),
                "actual role is not as expected");
        assertion.assertAll();
    }

    @Test(groups = "updatePlayer")
    public void shouldUpdateMultipleFields() {
        // create test player
        CreatePlayerRequestDto expectedTestPlayer = createValidPlayerHelper("user");
        Response expectedPlayerCreationResponse = httpClient.createPlayer(expectedTestPlayer, "supervisor");

        assertEquals(expectedPlayerCreationResponse.getStatusCode(), 200, "Status code mismatch");

        CreatePlayerResponseDto createdTestPlayer = expectedPlayerCreationResponse.as(CreatePlayerResponseDto.class);
        testPlayerId = createdTestPlayer.getId();

        // update player
        Map<String, Object> playerUpdateRequest = new HashMap<>();
        playerUpdateRequest.put("age", Long.valueOf(18L));
        playerUpdateRequest.put("gender", "female");
        playerUpdateRequest.put("login", "testLoginUserVcpt_updated");
        playerUpdateRequest.put("screenName", "screen_vcpt_updated");

        // request update player
        Response actualResponse = httpClient.updatePlayer(playerUpdateRequest, "supervisor", testPlayerId);
        assertEquals(actualResponse.getStatusCode(), 200);

        PlayerUpdateResponseDto actualUpdatedPlayer = actualResponse.as(PlayerUpdateResponseDto.class);

        SoftAssert assertion = new SoftAssert();
        assertion.assertEquals(actualUpdatedPlayer.getAge(), Integer.valueOf(18),"Actual age is not as expected");
        assertion.assertEquals(actualUpdatedPlayer.getGender(), playerUpdateRequest.get("gender"),"Actual gender is not as expected");
        assertion.assertEquals(actualUpdatedPlayer.getLogin(), playerUpdateRequest.get("login"),"actual login is not as expected");
        assertion.assertEquals(actualUpdatedPlayer.getScreenName(), playerUpdateRequest.get("screenName"),"actual screen name is not as expected");
        assertion.assertAll();
    }

    @Test(groups = "updatePlayer")
    public void failUpdateWhenEditorHasNoPermission() {
        // create test player
        CreatePlayerRequestDto expectedTestPlayer = createValidPlayerHelper("user");
        Response createPlayerResponse = httpClient.createPlayer(expectedTestPlayer, "supervisor");

        assertEquals(createPlayerResponse.getStatusCode(), 200, "Status code mismatch");

        CreatePlayerResponseDto createdTestPlayer = createPlayerResponse.as(CreatePlayerResponseDto.class);
        testPlayerId = createdTestPlayer.getId();

        // update player
        Map<String, Object> playerUpdateRequest = new HashMap<>();
        playerUpdateRequest.put("age", Long.valueOf(18L));
        playerUpdateRequest.put("gender", "female");
        playerUpdateRequest.put("login", "testLoginUserVcpt_updated");
        playerUpdateRequest.put("screenName", "screen_vcpt_updated");

        // request update player
        Response actualResponse = httpClient.updatePlayer(playerUpdateRequest, "admin", testPlayerId);
        assertEquals(actualResponse.getStatusCode(), 403, "Actual status code is not as expected");
    }

    @Test(groups = "updatePlayer")
    public void failUpdateWhenDataIsInvalid() {
        // create test player
        CreatePlayerRequestDto expectedTestPlayer = createValidPlayerHelper("user");
        Response createPlayerResponse = httpClient.createPlayer(expectedTestPlayer, "supervisor");

        assertEquals(createPlayerResponse.getStatusCode(), 200, "Status code mismatch");

        CreatePlayerResponseDto createdTestPlayer = createPlayerResponse.as(CreatePlayerResponseDto.class);
        testPlayerId = createdTestPlayer.getId();

        // update player
        Map<String, Object> playerUpdateRequest = new HashMap<>();
        playerUpdateRequest.put("age", Long.valueOf(0));
        playerUpdateRequest.put("gender", "");
        playerUpdateRequest.put("login", "");
        playerUpdateRequest.put("screenName", "");

        // request update player
        Response actualResponse = httpClient.updatePlayer(playerUpdateRequest, "supervisor", testPlayerId);
        assertEquals(actualResponse.getStatusCode(), 400, "Actual status code is not as expected");
    }

    @AfterMethod(alwaysRun = true)
    public void cleanup() {
        if (testPlayerId != null) {
            LOGGER.info("Cleanup: deleting player id={}", testPlayerId);
            httpClient.deletePlayer(testPlayerId, "supervisor");
            testPlayerId = null;
        }
    }

    private CreatePlayerRequestDto createValidPlayerHelper(String role) {
        CreatePlayerRequestDto player = new CreatePlayerRequestDto();
        player.setAge(25);
        player.setGender("male");
        player.setLogin("testLoginUserUcpt");
        player.setPassword("Test1234");
        player.setRole(role);
        player.setScreenName("screen_vcpt");
        return player;
    }
}

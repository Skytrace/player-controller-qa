package org.player.controller.qa;

import io.restassured.response.Response;
import org.player.controller.qa.base.BaseTest;
import org.player.controller.qa.dto.CreatePlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class VerifyDeletePlayerTest extends BaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(VerifyDeletePlayerTest.class);

    @Test
    public void deletePlayer() {
        CreatePlayer expectedPlayer = new CreatePlayer();
        expectedPlayer.setAge(25);
        expectedPlayer.setGender("male");
        expectedPlayer.setLogin("testLoginUserDp");
        expectedPlayer.setPassword("test1UserPassword!");
        expectedPlayer.setRole("admin");
        expectedPlayer.setScreenName("test_screen_cp");
        LOGGER.info("Expected test player prepared: {}", expectedPlayer);

        Response actualResult = httpClient.createPlayer(expectedPlayer, "supervisor");
        assertEquals(actualResult.getStatusCode(), 200, "Actual status code is not as expected");

        CreatePlayer actualPlayer = actualResult.as(CreatePlayer.class);
        assertTrue(actualPlayer.getId() > 0, "Incorrect userId value");

        LOGGER.info("Actual test player has been created: {}", actualPlayer);

        httpClient.deletePlayer(actualPlayer.getId(), "supervisor").then().statusCode(204);
    }

    @Test
    public void deletePLayerValidation() {
        httpClient.deletePlayer("notNumberId", "supervisor").then().statusCode(400);
    }

}

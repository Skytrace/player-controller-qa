package org.player.controller.qa;

import io.restassured.response.Response;
import org.player.controller.qa.base.BaseTest;
import org.player.controller.qa.dto.PlayerItem;
import org.player.controller.qa.dto.Players;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.testng.Assert.*;
import static org.testng.AssertJUnit.assertNotNull;


public class VerifyPlayerTest extends BaseTest {

    @Test
    public void userDatabaseShouldNotBeEmpty() {
        Response actualResult = httpClient.getPlayers();

        assertEquals(actualResult.getStatusCode(), 200, "Expected status code 200");

        Players responseData = actualResult.as(Players.class);
        Assert.assertNotNull(responseData.getPlayers(), "Players list should not be null");
        assertTrue(responseData.getPlayers().size() > 0, "Players list should contain more than 1 element");
    }

    @Test
    public void eachPlayerMustHaveRequiredFields() {
        Response response = httpClient.getPlayers();

        assertEquals(response.getStatusCode(), 200, "Expected status code 200");

        Players responseData = response.as(Players.class);
        List<PlayerItem> players = responseData.getPlayers();

        assertTrue(players.size() > 0, "Players list should not be empty");

        for (PlayerItem player : players) {
            assertTrue(player.getId() > 0, "Player id should be positive");
            assertNotNull(player.getScreenName(), "Player screenName should not be null");
            assertFalse(player.getScreenName().isEmpty(), "Player screenName should not be empty");
            assertNotNull(player.getGender(), "Player gender should not be null");
            assertFalse(player.getGender().isEmpty(), "Player gender should not be empty");
            assertTrue(player.getAge() > 0, "Player age should be positive");
        }
    }

    @Test
    public void eachPlayerMustHaveUniqueId() {
        Response response = httpClient.getPlayers();
        assertEquals(response.getStatusCode(), 200, "Expected status code 200");

        Players responseData = response.as(Players.class);
        List<PlayerItem> players = responseData.getPlayers();

        Assert.assertTrue(players.size() > 0, "Players list should not be empty");

        Set<Long> ids = new HashSet<>();
        for (PlayerItem player : players) {
            ids.add(player.getId());
        }

        assertEquals(ids.size(), players.size(), "Duplicate IDs found in players list");
    }
}

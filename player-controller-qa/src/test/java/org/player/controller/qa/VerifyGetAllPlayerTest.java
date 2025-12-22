package org.player.controller.qa;

import io.restassured.response.Response;
import org.player.controller.qa.base.BaseTest;
import org.player.controller.qa.dto.PlayerItemResponseDto;
import org.player.controller.qa.dto.PlayersResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.testng.Assert.*;
import static org.testng.AssertJUnit.assertNotNull;


public class VerifyGetAllPlayerTest extends BaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(VerifyGetAllPlayerTest.class);

    @Test
    public void userDatabaseShouldNotBeEmpty() {
        Response actualResult = getHttpClient().getPlayers();

        assertEquals(actualResult.getStatusCode(), 200, "Expected status code 200");

        PlayersResponseDto responseData = actualResult.as(PlayersResponseDto.class);
        Assert.assertNotNull(responseData.getPlayers(), "Players list should not be null");
        assertTrue(responseData.getPlayers().size() > 0, "Players list should contain more than 1 element");

        LOGGER.info("the total count of all players is: {}", responseData.getPlayers().size());
    }

    @Test
    public void eachPlayerMustHaveRequiredFields() {
        Response actualResponse = getHttpClient().getPlayers();

        assertEquals(actualResponse.getStatusCode(), 200, "Expected status code 200");

        PlayersResponseDto actualPlayersResponse = actualResponse.as(PlayersResponseDto.class);
        List<PlayerItemResponseDto> actualPlayers = actualPlayersResponse.getPlayers();

        assertTrue(actualPlayers.size() > 0, "Players list should not be empty");

        for (PlayerItemResponseDto player : actualPlayers) {
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
        Response response = getHttpClient().getPlayers();
        assertEquals(response.getStatusCode(), 200, "Expected status code 200");

        PlayersResponseDto responseData = response.as(PlayersResponseDto.class);
        List<PlayerItemResponseDto> players = responseData.getPlayers();

        Assert.assertTrue(players.size() > 0, "Players list should not be empty");

        Set<Long> ids = new HashSet<>();
        for (PlayerItemResponseDto player : players) {
            ids.add(player.getId());
        }

        assertEquals(ids.size(), players.size(), "Duplicate IDs found in players list");
    }
}

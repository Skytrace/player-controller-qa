package org.player.controller.qa;

import io.restassured.response.Response;
import org.player.controller.qa.base.BaseTest;
import org.player.controller.qa.dto.CreatePlayerRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class VerifyDeletePlayerTest extends BaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(VerifyDeletePlayerTest.class);
    private Long testPlayerId;

    @Test
    public void deletePlayerById() {
        CreatePlayerRequestDto expectedPlayer = createValidPlayerHelper();
        expectedPlayer.setLogin("testLoginUserDpById");

        Response actualResult = httpClient.createPlayer(expectedPlayer, "supervisor");
        assertEquals(actualResult.getStatusCode(), 200, "Actual status code is not as expected");

        CreatePlayerRequestDto actualPlayer = actualResult.as(CreatePlayerRequestDto.class);
        assertTrue(actualPlayer.getId() > 0, "Incorrect userId value");

        LOGGER.info("Actual test player has been created: {}", actualPlayer);
        int actualStatusCode = httpClient.deletePlayer(actualPlayer.getId(), "supervisor").statusCode();

        Assert.assertEquals(actualStatusCode, 204, "Delete User: Actual status code is not as expected");
    }

    @Test(groups = "deletePlayerByIdAsString")
    public void deletePlayerByIdAsString() {
        CreatePlayerRequestDto expectedPlayer = createValidPlayerHelper();
        expectedPlayer.setLogin("testLoginUserDpByIdS");

        Response actualResult = httpClient.createPlayer(expectedPlayer, "supervisor");
        assertEquals(actualResult.getStatusCode(), 200, "Actual status code is not as expected");

        CreatePlayerRequestDto actualPlayer = actualResult.as(CreatePlayerRequestDto.class);
        testPlayerId = actualPlayer.getId();
        LOGGER.info("Actual test player has been created: {}", actualPlayer);

        // here passing the player id as a string
        httpClient.deletePlayer(String.valueOf(actualPlayer.getId()), "supervisor").then().statusCode(400);
    }

    @Test
    public void deleteNonExistedPlayerById() {
        CreatePlayerRequestDto expectedPlayer = createValidPlayerHelper();
        expectedPlayer.setLogin("testLoginUserDnepById");

        Response actualResult = httpClient.createPlayer(expectedPlayer, "supervisor");
        assertEquals(actualResult.getStatusCode(), 200, "Actual status code is not as expected");

        CreatePlayerRequestDto actualPlayer = actualResult.as(CreatePlayerRequestDto.class);
        assertTrue(actualPlayer.getId() > 0, "Incorrect userId value");

        LOGGER.info("Actual test player has been created: {}", actualPlayer);

        httpClient.deletePlayer(actualPlayer.getId(), "supervisor").then().statusCode(204);

        int actualStatusCode = httpClient.deletePlayer(actualPlayer.getId(), "supervisor").statusCode();
        Assert.assertEquals(actualStatusCode, 404, "Actual status code for deleting non existed user " +
                "is not as expected");

    }

    @Test(dataProvider = "getValidationDataForDeletePlayerTest")
    public void deletePlayerValidation(Object id) {
        int expectedStatusCode = 400; // bad request
        int actualStatusCode = httpClient.deletePlayer(id, "supervisor").statusCode();

        Assert.assertEquals(actualStatusCode, expectedStatusCode, "Actual status code is not as expected");
    }

    @AfterTest(groups = "deletePlayerByIdAsString")
    public void cleanPlayerFromDeletePlayerByIdAsStringTest() {
        LOGGER.info("Time to delete test player with id {}", testPlayerId);
        if (testPlayerId != null) {
            httpClient.deletePlayer(testPlayerId, "supervisor").then().statusCode(204);
            LOGGER.info("Test User with id - {}, successfully deleted", testPlayerId);
        }
    }

    @DataProvider
    public Object[][] getValidationDataForDeletePlayerTest() {
        return new Object[][]{
                { "someString" }, // checking on String
                { null },          // checking on null
                { "" }             // checking on empty String
        };
    }

    private CreatePlayerRequestDto createValidPlayerHelper() {
        CreatePlayerRequestDto expectedPlayer = new CreatePlayerRequestDto();
        expectedPlayer.setAge(25);
        expectedPlayer.setGender("male");
        expectedPlayer.setLogin("testLoginUserDp");
        expectedPlayer.setPassword("test1UserPassword!");
        expectedPlayer.setRole("user");
        expectedPlayer.setScreenName("test_screen_dpById");
        LOGGER.info("Expected test player prepared: {}", expectedPlayer);
        return expectedPlayer;
    }

}

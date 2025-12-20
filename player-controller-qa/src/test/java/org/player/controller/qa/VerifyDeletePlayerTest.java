package org.player.controller.qa;

import io.restassured.response.Response;
import org.player.controller.qa.base.BaseTest;
import org.player.controller.qa.dto.CreatePlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class VerifyDeletePlayerTest extends BaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(VerifyDeletePlayerTest.class);
    private Long testPlayerLoginDpByIdS;

    @Test
    public void deletePlayerById() {
        CreatePlayer expectedPlayer = new CreatePlayer();
        expectedPlayer.setAge(25);
        expectedPlayer.setGender("male");
        expectedPlayer.setLogin("testLoginUserDp");
        expectedPlayer.setPassword("test1UserPassword!");
        expectedPlayer.setRole("admin");
        expectedPlayer.setScreenName("test_screen_dpById");
        LOGGER.info("Expected test player prepared: {}", expectedPlayer);

        Response actualResult = httpClient.createPlayer(expectedPlayer, "supervisor");
        assertEquals(actualResult.getStatusCode(), 200, "Actual status code is not as expected");

        CreatePlayer actualPlayer = actualResult.as(CreatePlayer.class);
        assertTrue(actualPlayer.getId() > 0, "Incorrect userId value");

        LOGGER.info("Actual test player has been created: {}", actualPlayer);

        httpClient.deletePlayer(actualPlayer.getId(), "supervisor").then().statusCode(204);
    }

    @Test(groups = "deletePlayerByIdAsString")
    public void deletePlayerByIdAsString() {
        CreatePlayer expectedPlayer = new CreatePlayer();
        expectedPlayer.setAge(25);
        expectedPlayer.setGender("male");
        expectedPlayer.setLogin("testLoginUserDpByIdS");
        expectedPlayer.setPassword("test1UserPassword!");
        expectedPlayer.setRole("admin");
        expectedPlayer.setScreenName("test_screen_DpByIdS");
        LOGGER.info("Expected test player prepared: {}", expectedPlayer);

        Response actualResult = httpClient.createPlayer(expectedPlayer, "supervisor");
        assertEquals(actualResult.getStatusCode(), 200, "Actual status code is not as expected");

        CreatePlayer actualPlayer = actualResult.as(CreatePlayer.class);
        testPlayerLoginDpByIdS = actualPlayer.getId();
        LOGGER.info("Actual test player has been created: {}", actualPlayer);

        // here passing the player id as a string
        httpClient.deletePlayer(String.valueOf(actualPlayer.getId()), "supervisor").then().statusCode(400);
    }

    @AfterTest(groups = "deletePlayerByIdAsString")
    public void cleanPlayerFromDeletePlayerByIdAsStringTest() {
        LOGGER.info("Time to delete test player with id {}", testPlayerLoginDpByIdS);
        if (testPlayerLoginDpByIdS != null) {
            httpClient.deletePlayer(testPlayerLoginDpByIdS, "supervisor").then().statusCode(204);
            LOGGER.info("Test User with id - {}, successfully deleted", testPlayerLoginDpByIdS);
        }
    }

    @Test
    public void deleteNonExistedPlayerById() {
        CreatePlayer expectedPlayer = new CreatePlayer();
        expectedPlayer.setAge(20);
        expectedPlayer.setGender("female");
        expectedPlayer.setLogin("testLoginUserDnepById");
        expectedPlayer.setPassword("test1UserPassword!");
        expectedPlayer.setRole("admin");
        expectedPlayer.setScreenName("test_screen_Dnep");
        LOGGER.info("Expected test player prepared: {}", expectedPlayer);

        Response actualResult = httpClient.createPlayer(expectedPlayer, "supervisor");
        assertEquals(actualResult.getStatusCode(), 200, "Actual status code is not as expected");

        CreatePlayer actualPlayer = actualResult.as(CreatePlayer.class);
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

    @DataProvider
    public Object[][] getValidationDataForDeletePlayerTest() {
        return new Object[][]{
                { "notNumberId" }, // checking on String
                { null },          // checking on null
                { "" }             // checking on empty String
        };
    }

}

package org.player.controller.qa;

import io.restassured.response.Response;
import org.player.controller.qa.base.BaseTest;
import org.player.controller.qa.dto.CreatePlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class VerifyCreatePlayerTest extends BaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(VerifyCreatePlayerTest.class);

    private Long createdPlayerUserId;

    //TODO Think about password and create via GET
    @Test(groups = "createPlayer")
    public void createPlayer() {
        CreatePlayer expectedPlayer = new CreatePlayer();
        expectedPlayer.setAge(25);
        expectedPlayer.setGender("male");
        expectedPlayer.setLogin("testLoginUserCp");
        expectedPlayer.setPassword("test1UserPassword!");
        expectedPlayer.setRole("admin");
        expectedPlayer.setScreenName("test_screen_cp");
        LOGGER.info("Expected test player prepared: {}", expectedPlayer);

        Response actualResult = httpClient.createPlayer(expectedPlayer, "supervisor");
        assertEquals(actualResult.getStatusCode(), 200, "Actual status code is not as expected");

        CreatePlayer actualPlayer = actualResult.as(CreatePlayer.class);
        assertTrue(actualPlayer.getId() > 0, "Incorrect userId value");

        LOGGER.info("Actual test player has been created: {}", actualPlayer);

        createdPlayerUserId = actualPlayer.getId();

        SoftAssert assertion = new SoftAssert();

        assertion.assertEquals(actualPlayer.getAge(), expectedPlayer.getAge(), "Actual player age is not as expected");
        assertion.assertEquals(actualPlayer.getGender(), expectedPlayer.getGender(), "Actual player gender is not as expected");
        assertion.assertEquals(actualPlayer.getLogin(), expectedPlayer.getLogin(), "Actual player login is not as expected");
        assertion.assertEquals(actualPlayer.getRole(), expectedPlayer.getRole(), "Actual player role is not as expected");
        assertion.assertEquals(actualPlayer.getScreenName(), expectedPlayer.getScreenName(), "Actual player screenName is not as expected");

        assertion.assertAll();

        LOGGER.info("Create Player test has been passed");
    }

    //TODO Think about delete user after creation due to prod issue
    @AfterTest(groups = "createPlayer")
    public void deleteTestUser() {
        LOGGER.info("Time to delete test player with id {}", createdPlayerUserId);
        if (createdPlayerUserId != null) {
            httpClient.deletePlayer(createdPlayerUserId, "supervisor").then().statusCode(204);
            LOGGER.info("Test User with id - {}, successfully deleted", createdPlayerUserId);
        }
    }

}

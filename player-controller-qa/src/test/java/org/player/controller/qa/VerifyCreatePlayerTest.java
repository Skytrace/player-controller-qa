package org.player.controller.qa;

import io.restassured.response.Response;
import org.player.controller.qa.base.BaseTest;
import org.player.controller.qa.dto.CreatePlayerRequestDto;
import org.player.controller.qa.dto.CreatePlayerResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class VerifyCreatePlayerTest extends BaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(VerifyCreatePlayerTest.class);
    private Long createdPlayerId;

    @Test(groups = "createPlayer")
    public void shouldCreatePlayerWithAdminRole() {
        CreatePlayerRequestDto expectedPlayer = createValidPlayerHelper("admin");
        Response createPlayerResponse = getHttpClient().createPlayer(expectedPlayer, "supervisor");

        assertEquals(createPlayerResponse.getStatusCode(), 200, "Actual status code is not as expected");

        CreatePlayerResponseDto actualPlayer = createPlayerResponse.as(CreatePlayerResponseDto.class);
        createdPlayerId = actualPlayer.getId();

        assertTrue(createdPlayerId != null && createdPlayerId > 0, "Player id is invalid");

        SoftAssert assertion = new SoftAssert();
        assertion.assertEquals(actualPlayer.getAge(), expectedPlayer.getAge(), "Actual age is not as expected");
        assertion.assertEquals(actualPlayer.getGender(), expectedPlayer.getGender(), "Actual gender is not as expected");
        assertion.assertEquals(actualPlayer.getLogin(), expectedPlayer.getLogin(), "Actual login is not as expected");
        assertion.assertEquals(actualPlayer.getRole(), expectedPlayer.getRole(), "Actual role is not as expected");
        assertion.assertEquals(actualPlayer.getScreenName(), expectedPlayer.getScreenName(), "Actual screen name is not as expected");
        assertion.assertAll();
    }

    @Test(groups = "createPlayer")
    public void shouldCreatePlayerValidUserRole() {
        CreatePlayerRequestDto expectedPlayer = createValidPlayerHelper("user");
        Response createPlayerResponse = getHttpClient().createPlayer(expectedPlayer, "supervisor");

        assertEquals(createPlayerResponse.getStatusCode(), 200, "Actual status code is not as expected");

        CreatePlayerResponseDto actualPlayer = createPlayerResponse.as(CreatePlayerResponseDto.class);
        createdPlayerId = actualPlayer.getId();

        assertTrue(createdPlayerId != null && createdPlayerId > 0);

        SoftAssert assertion = new SoftAssert();
        assertion.assertEquals(actualPlayer.getAge(), expectedPlayer.getAge(), "Actual age is not as expected");
        assertion.assertEquals(actualPlayer.getGender(), expectedPlayer.getGender(), "Actual gender is not as expected");
        assertion.assertEquals(actualPlayer.getLogin(), expectedPlayer.getLogin(), "Actual login is not as expected");
        assertion.assertEquals(actualPlayer.getRole(), expectedPlayer.getRole(), "Actual role is not as expected");
        assertion.assertEquals(actualPlayer.getScreenName(), expectedPlayer.getScreenName(), "Actual screen name is not as expected");
        assertion.assertAll();
    }

    @Test(groups = "createPlayer")
    public void shouldFailCreateWhenAgeIsLowest() {
        CreatePlayerRequestDto expectedPlayer = createValidPlayerHelper("user");
        expectedPlayer.setAge(16);

        Response actualResponse = getHttpClient().createPlayer(expectedPlayer, "admin");
        captureUnexpectedCreation(actualResponse);

        assertTrue(actualResponse.getStatusCode() == 400 || actualResponse.getStatusCode() == 403,
                "Expected validation error for age = 16 but got " + actualResponse.getStatusCode());
    }

    @Test(groups = "createPlayer")
    public void shouldFailCreateWhenAgeIsHighest() {
        CreatePlayerRequestDto expectedPlayer = createValidPlayerHelper("user");
        expectedPlayer.setAge(60);

        Response actualResponse = getHttpClient().createPlayer(expectedPlayer, "admin");
        captureUnexpectedCreation(actualResponse);

        assertTrue(actualResponse.getStatusCode() == 400 || actualResponse.getStatusCode() == 403,
                "Expected validation error for age=60 but got " + actualResponse.getStatusCode());
    }

    @Test(groups = "createPlayer")
    public void shouldFailCreateWhenGenderIsInvalid() {
        CreatePlayerRequestDto expectedPlayer = createValidPlayerHelper("user");
        expectedPlayer.setGender("unknown");

        Response actualResponse = getHttpClient().createPlayer(expectedPlayer, "admin");
        captureUnexpectedCreation(actualResponse);

        assertTrue(actualResponse.getStatusCode() == 400 || actualResponse.getStatusCode() == 403,
                "Expected validation error for gender but got " + actualResponse.getStatusCode());
    }

    @Test(groups = "createPlayer")
    public void shouldFailCreateWhenGenderIsUppercase() {
        CreatePlayerRequestDto expectedPlayer = createValidPlayerHelper("user");
        expectedPlayer.setGender("MALE");

        Response actualResponse = getHttpClient().createPlayer(expectedPlayer, "admin");
        captureUnexpectedCreation(actualResponse);

        assertTrue(actualResponse.getStatusCode() == 400 || actualResponse.getStatusCode() == 403,
                "Expected validation error for gender=MALE but got " + actualResponse.getStatusCode());
    }

    @Test(groups = "createPlayer")
    public void shouldFailCreateWhenRoleIsInvalid() {
        CreatePlayerRequestDto expectedPlayer = createValidPlayerHelper("randomRole");

        Response actualResponse = getHttpClient().createPlayer(expectedPlayer, "admin");
        captureUnexpectedCreation(actualResponse);

        assertTrue(actualResponse.getStatusCode() == 400 || actualResponse.getStatusCode() == 403,
                "Expected validation error for role but got " + actualResponse.getStatusCode());
    }

    @Test(groups = "createPlayer")
    public void shouldFailCreateWhenEditorHasNoPermission() {
        CreatePlayerRequestDto expectedPlayer = createValidPlayerHelper("user");

        Response actualResponse = getHttpClient().createPlayer(expectedPlayer, "user");
        captureUnexpectedCreation(actualResponse);

        assertEquals(actualResponse.getStatusCode(), 403,
                "Expected 403 for editor=user but got " + actualResponse.getStatusCode());
    }

    @Test(groups = "createPlayer")
    public void shouldFailCreateWhenPasswordTooShort() {
        CreatePlayerRequestDto expectedPlayer = createValidPlayerHelper("user");
        expectedPlayer.setPassword("123");

        Response actualResponse = getHttpClient().createPlayer(expectedPlayer, "admin");
        captureUnexpectedCreation(actualResponse);

        assertTrue(actualResponse.getStatusCode() == 400 || actualResponse.getStatusCode() == 403,
                "Expected validation error for password too short but got " + actualResponse.getStatusCode());
    }

    @Test(groups = "createPlayer")
    public void shouldFailCreateWhenPasswordHasNoDigits() {
        CreatePlayerRequestDto expectedPlayer = createValidPlayerHelper("user");
        expectedPlayer.setPassword("abcdefg"); // 7 chars, but no digits

        Response actualResponse = getHttpClient().createPlayer(expectedPlayer, "supervisor");
        captureUnexpectedCreation(actualResponse);

        assertTrue(actualResponse.getStatusCode() == 400 || actualResponse.getStatusCode() == 403,
                "Expected validation error for password without digits but got " + actualResponse.getStatusCode());
    }

    @Test(groups = "createPlayer")
    public void shouldFailCreateWhenPasswordHasNoLetters() {
        CreatePlayerRequestDto expectedPlayer = createValidPlayerHelper("user");
        expectedPlayer.setPassword("1234567"); // 7 chars, but no letters

        Response actualResponse = getHttpClient().createPlayer(expectedPlayer, "supervisor");
        captureUnexpectedCreation(actualResponse);

        assertTrue(actualResponse.getStatusCode() == 400 || actualResponse.getStatusCode() == 403,
                "Expected validation error for password without letters but got " + actualResponse.getStatusCode());
    }

    @Test(groups = "createPlayer")
    public void shouldFailCreateWhenPasswordHasNonLatin() {
        CreatePlayerRequestDto expectedPlayer = createValidPlayerHelper("user");
        expectedPlayer.setPassword("пароль123");

        Response actualResponse = getHttpClient().createPlayer(expectedPlayer, "supervisor");
        captureUnexpectedCreation(actualResponse);

        assertTrue(actualResponse.getStatusCode() == 400 || actualResponse.getStatusCode() == 403,
                "Expected validation error for password non-latin but got " + actualResponse.getStatusCode());
    }

    @Test(groups = "createPlayer")
    public void shouldFailCreateWhenLoginIsNotUnique() {
        CreatePlayerRequestDto firstPlayer = createValidPlayerHelper("user");

        Response creatFirstUserActualResponse = getHttpClient().createPlayer(firstPlayer, "supervisor");
        assertEquals(creatFirstUserActualResponse.getStatusCode(), 200, "Precondition user wasn't created");

        Long firstId = creatFirstUserActualResponse.as(CreatePlayerResponseDto.class).getId();
        createdPlayerId = firstId;

        // create second user with assertionme login
        CreatePlayerRequestDto secondPlayer = createValidPlayerHelper("user");
        Response creatSecondUserActualResponse = getHttpClient().createPlayer(secondPlayer, "admin");

        // BUG: if API still created it
        captureUnexpectedCreation(creatSecondUserActualResponse);

        assertTrue(creatSecondUserActualResponse.getStatusCode() == 400 ||
                        creatSecondUserActualResponse.getStatusCode() == 403 ||
                        creatSecondUserActualResponse.getStatusCode() == 409,
                "Expected validation error for duplicate login but got " + creatSecondUserActualResponse.getStatusCode());
    }

    @Test(groups = "createPlayer")
    public void shouldFailCreateWhenScreenNameIsNotUnique() {
        // create first user
        CreatePlayerRequestDto first = createValidPlayerHelper("user");
        Response creatFirstUserActualResponse = getHttpClient().createPlayer(first, "supervisor");
        assertEquals(creatFirstUserActualResponse.getStatusCode(), 200, "Precondition user wasn't created");

        Long firstPlayerId = creatFirstUserActualResponse.as(CreatePlayerResponseDto.class).getId();
        createdPlayerId = firstPlayerId;

        // create second user with assertionme screenName
        CreatePlayerRequestDto second = createValidPlayerHelper("user");
        Response secondPlayerId = getHttpClient().createPlayer(second, "admin");

        // BUG: if API still created it
        captureUnexpectedCreation(secondPlayerId);

        assertTrue(secondPlayerId.getStatusCode() == 400 ||
                        secondPlayerId.getStatusCode() == 403 ||
                        secondPlayerId.getStatusCode() == 409,
                "Expected validation error for duplicate screenName but got " +
                        secondPlayerId.getStatusCode());
    }

    @AfterMethod(alwaysRun = true)
    public void deleteCreatedPlayer() {
        if (createdPlayerId != null) {
            LOGGER.info("Cleanup: deleting test player id = {}", createdPlayerId);
            Response deleteResponse = getHttpClient().deletePlayer(createdPlayerId, "supervisor");
            LOGGER.info("Cleanup delete statusCode = {}", deleteResponse.getStatusCode());
        }
    }

    private CreatePlayerRequestDto createValidPlayerHelper(String role) {
        CreatePlayerRequestDto player = new CreatePlayerRequestDto();
        player.setAge(25);
        player.setGender("male");
        player.setLogin("testLoginUserVcpt");
        player.setPassword("Test1234");
        player.setRole(role);
        player.setScreenName("screen_vcpt");
        return player;
    }

    /**
     * If API returns 200 on a negative scenario (BUG),
     * capture id to delete in @AfterMethod.
     */
    private void captureUnexpectedCreation(Response response) {
        if (response != null && response.getStatusCode() == 200) {
            CreatePlayerResponseDto created = response.as(CreatePlayerResponseDto.class);
            createdPlayerId = created.getId();
            LOGGER.error("Player created unexpectedly. id={}", createdPlayerId);
        }
    }
}
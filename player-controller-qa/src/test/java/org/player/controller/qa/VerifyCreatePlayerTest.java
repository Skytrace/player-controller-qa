package org.player.controller.qa;

import io.restassured.response.Response;
import org.player.controller.qa.base.BaseTest;
import org.player.controller.qa.dto.CreatePlayerRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class VerifyCreatePlayerTest extends BaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(VerifyCreatePlayerTest.class);
    private Long createdPlayerUserId;

    /* =========================
       POSITIVE
       ========================= */

    @Test(groups = "createPlayer")
    public void shouldCreatePlayerValidAdminRole() {
        CreatePlayerRequestDto expected = validPlayer("admin");
        Response response = httpClient.createPlayer(expected, "supervisor");

        assertEquals(response.getStatusCode(), 200, "Status code mismatch");

        CreatePlayerRequestDto actual = response.as(CreatePlayerRequestDto.class);
        createdPlayerUserId = actual.getId();

        assertTrue(createdPlayerUserId != null && createdPlayerUserId > 0, "Player id is invalid");

        SoftAssert assertion = new SoftAssert();
        assertion.assertEquals(actual.getAge(), expected.getAge(), "Age mismatch");
        assertion.assertEquals(actual.getGender(), expected.getGender(), "Gender mismatch");
        assertion.assertEquals(actual.getLogin(), expected.getLogin(), "Login mismatch");
        assertion.assertEquals(actual.getRole(), expected.getRole(), "Role mismatch");
        assertion.assertEquals(actual.getScreenName(), expected.getScreenName(), "ScreenName mismatch");
        assertion.assertAll();
    }

    @Test(groups = "createPlayer")
    public void shouldCreatePlayerValidUserRole() {
        CreatePlayerRequestDto expected = validPlayer("user");
        Response response = httpClient.createPlayer(expected, "supervisor");

        assertEquals(response.getStatusCode(), 200);

        CreatePlayerRequestDto actual = response.as(CreatePlayerRequestDto.class);
        createdPlayerUserId = actual.getId();

        assertTrue(createdPlayerUserId != null && createdPlayerUserId > 0);
    }

    /* =========================
       NEGATIVE: AGE ( >16 and <60 )
       ========================= */

    @Test(groups = "createPlayer")
    public void shouldFailCreateWhenAgeIsLowest() {
        CreatePlayerRequestDto expectedPlayer = validPlayer("user");
        expectedPlayer.setAge(16);

        Response actualResponse = httpClient.createPlayer(expectedPlayer, "admin");
        captureUnexpectedCreation(actualResponse);

        assertTrue(actualResponse.getStatusCode() == 400 || actualResponse.getStatusCode() == 403,
                "Expected validation error for age=16 but got " + actualResponse.getStatusCode());
    }

    @Test(groups = "createPlayer")
    public void shouldFailCreateWhenAgeIsHighest() {
        CreatePlayerRequestDto expectedPlayer = validPlayer("user");
        expectedPlayer.setAge(60);

        Response actualResponse = httpClient.createPlayer(expectedPlayer, "admin");
        captureUnexpectedCreation(actualResponse);

        assertTrue(actualResponse.getStatusCode() == 400 || actualResponse.getStatusCode() == 403,
                "Expected validation error for age=60 but got " + actualResponse.getStatusCode());
    }

    /* =========================
       NEGATIVE: GENDER ( male/female )
       ========================= */

    @Test(groups = "createPlayer")
    public void shouldFailCreateWhenGenderIsInvalid() {
        CreatePlayerRequestDto expectedPlayer = validPlayer("user");
        expectedPlayer.setGender("unknown");

        Response actualResponse = httpClient.createPlayer(expectedPlayer, "admin");
        captureUnexpectedCreation(actualResponse);

        assertTrue(actualResponse.getStatusCode() == 400 || actualResponse.getStatusCode() == 403,
                "Expected validation error for gender but got " + actualResponse.getStatusCode());
    }

    @Test(groups = "createPlayer")
    public void shouldFailCreateWhenGenderIsUppercase() {
        CreatePlayerRequestDto expectedPlayer = validPlayer("user");
        expectedPlayer.setGender("MALE");

        Response actualResponse = httpClient.createPlayer(expectedPlayer, "admin");
        captureUnexpectedCreation(actualResponse);

        assertTrue(actualResponse.getStatusCode() == 400 || actualResponse.getStatusCode() == 403,
                "Expected validation error for gender=MALE but got " + actualResponse.getStatusCode());
    }

    /* =========================
       NEGATIVE: ROLE ( admin/user only )
       ========================= */

    @Test(groups = "createPlayer")
    public void shouldFailCreateWhenRoleIsInvalid() {
        CreatePlayerRequestDto expectedPlayer = validPlayer("randomRole");

        Response actualResponse = httpClient.createPlayer(expectedPlayer, "admin");
        captureUnexpectedCreation(actualResponse);

        assertTrue(actualResponse.getStatusCode() == 400 || actualResponse.getStatusCode() == 403,
                "Expected validation error for role but got " + actualResponse.getStatusCode());
    }

    /* =========================
       NEGATIVE: EDITOR ( only supervisor/admin can create )
       ========================= */

    @Test(groups = "createPlayer")
    public void shouldFailCreateWhenEditorHasNoPermission() {
        CreatePlayerRequestDto expectedPlayer = validPlayer("user");

        Response actualResponse = httpClient.createPlayer(expectedPlayer, "user");
        captureUnexpectedCreation(actualResponse);

        assertEquals(actualResponse.getStatusCode(), 403,
                "Expected 403 for editor=user but got " + actualResponse.getStatusCode());
    }

    /* =========================
       NEGATIVE: PASSWORD ( latin letters + digits, 7..15 )
       ========================= */

    @Test(groups = "createPlayer")
    public void shouldFailCreateWhenPasswordTooShort() {
        CreatePlayerRequestDto expectedPlayer = validPlayer("user");
        expectedPlayer.setPassword("123");

        Response actualResponse = httpClient.createPlayer(expectedPlayer, "admin");
        captureUnexpectedCreation(actualResponse);

        assertTrue(actualResponse.getStatusCode() == 400 || actualResponse.getStatusCode() == 403,
                "Expected validation error for password too short but got " + actualResponse.getStatusCode());
    }

    @Test(groups = "createPlayer")
    public void shouldFailCreateWhenPasswordHasNoDigits() {
        CreatePlayerRequestDto expectedPlayer = validPlayer("user");
        expectedPlayer.setPassword("abcdefg"); // 7 chars, but no digits

        Response actualResponse = httpClient.createPlayer(expectedPlayer, "supervisor");
        captureUnexpectedCreation(actualResponse);

        assertTrue(actualResponse.getStatusCode() == 400 || actualResponse.getStatusCode() == 403,
                "Expected validation error for password without digits but got " + actualResponse.getStatusCode());
    }

    @Test(groups = "createPlayer")
    public void shouldFailCreateWhenPasswordHasNoLetters() {
        CreatePlayerRequestDto expectedPlayer = validPlayer("user");
        expectedPlayer.setPassword("1234567"); // 7 chars, but no letters

        Response actualResponse = httpClient.createPlayer(expectedPlayer, "supervisor");
        captureUnexpectedCreation(actualResponse);

        assertTrue(actualResponse.getStatusCode() == 400 || actualResponse.getStatusCode() == 403,
                "Expected validation error for password without letters but got " + actualResponse.getStatusCode());
    }

    @Test(groups = "createPlayer")
    public void shouldFailCreateWhenPasswordHasNonLatin() {
        CreatePlayerRequestDto expectedPlayer = validPlayer("user");
        expectedPlayer.setPassword("пароль123");

        Response actualResponse = httpClient.createPlayer(expectedPlayer, "supervisor");
        captureUnexpectedCreation(actualResponse);

        assertTrue(actualResponse.getStatusCode() == 400 || actualResponse.getStatusCode() == 403,
                "Expected validation error for password non-latin but got " + actualResponse.getStatusCode());
    }

    /* =========================
       NEGATIVE: LOGIN UNIQUE
       ========================= */

    @Test(groups = "createPlayer")
    public void shouldFailCreateWhenLoginIsNotUnique() {
        String dupLogin = "dup_login_" + System.currentTimeMillis();

        // create first user
        CreatePlayerRequestDto first = validPlayer("user");
        first.setLogin(dupLogin);

        Response creatFirstUserActualResponse = httpClient.createPlayer(first, "supervisor");
        assertEquals(creatFirstUserActualResponse.getStatusCode(), 200, "Precondition user wasn't created");

        Long firstId = creatFirstUserActualResponse.as(CreatePlayerRequestDto.class).getId();
        // if anything goes wrong later, we still should cleanup this one:
        createdPlayerUserId = firstId;

        // create second user with assertionme login
        CreatePlayerRequestDto second = validPlayer("user");
        second.setLogin(dupLogin);

        Response creatSecondUserActualResponse = httpClient.createPlayer(second, "admin");

        // BUG: if API still created it
        captureUnexpectedCreation(creatSecondUserActualResponse);

        assertTrue(creatSecondUserActualResponse.getStatusCode() == 400 ||
                        creatSecondUserActualResponse.getStatusCode() == 403 ||
                        creatSecondUserActualResponse.getStatusCode() == 409,
                "Expected validation error for duplicate login but got " + creatSecondUserActualResponse.getStatusCode());
    }

    /* =========================
       NEGATIVE: SCREEN NAME UNIQUE
       ========================= */

    @Test(groups = "createPlayer")
    public void shouldFailCreateWhenScreenNameIsNotUnique() {
        String dupScreen = "dup_screen_" + System.currentTimeMillis();

        // create first user
        CreatePlayerRequestDto first = validPlayer("user");
        first.setScreenName(dupScreen);

        Response creatFirstUserActualResponse = httpClient.createPlayer(first, "supervisor");
        assertEquals(creatFirstUserActualResponse.getStatusCode(), 200, "Precondition user wasn't created");

        Long firstPlayerId = creatFirstUserActualResponse.as(CreatePlayerRequestDto.class).getId();
        createdPlayerUserId = firstPlayerId;

        // create second user with assertionme screenName
        CreatePlayerRequestDto second = validPlayer("user");
        second.setScreenName(dupScreen);

        Response secondPlayerId = httpClient.createPlayer(second, "admin");

        // BUG: if API still created it
        captureUnexpectedCreation(secondPlayerId);

        assertTrue(secondPlayerId.getStatusCode() == 400 ||
                        secondPlayerId.getStatusCode() == 403 ||
                        secondPlayerId.getStatusCode() == 409,
                "Expected validation error for duplicate screenName but got " +
                        secondPlayerId.getStatusCode());
    }

    /* =========================
       CLEANUP: after EACH test
       ========================= */

    @AfterMethod(alwaysRun = true)
    public void deleteCreatedPlayer() {
        if (createdPlayerUserId != null) {
            LOGGER.info("Cleanup: deleting test player id = {}", createdPlayerUserId);
            Response deleteResponse = httpClient.deletePlayer(createdPlayerUserId, "supervisor");
            LOGGER.info("Cleanup delete statusCode = {}", deleteResponse.getStatusCode());
        }
    }

    /* =========================
       HELPERS
       ========================= */

    private CreatePlayerRequestDto validPlayer(String role) {
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
            CreatePlayerRequestDto created = response.as(CreatePlayerRequestDto.class);
            createdPlayerUserId = created.getId();
            LOGGER.error("Player created unexpectedly. id={}", createdPlayerUserId);
        }
    }
}
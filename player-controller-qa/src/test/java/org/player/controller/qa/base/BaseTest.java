package org.player.controller.qa.base;

import org.player.controller.qa.http.PlayerHttpClient;

public class BaseTest {
    private static ThreadLocal<PlayerHttpClient> httpClient =
            ThreadLocal.withInitial(PlayerHttpClient::new);

    protected static PlayerHttpClient getHttpClient() {
        return httpClient.get();
    }

}

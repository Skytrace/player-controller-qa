package org.player.controller.qa.dto;

import java.util.List;

public class Players {

    List<PlayerItem> players;

    public List<PlayerItem> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerItem> players) {
        this.players = players;
    }

    @Override
    public String toString() {
        return "Players{" +
                "players=" + players +
                '}';
    }
}

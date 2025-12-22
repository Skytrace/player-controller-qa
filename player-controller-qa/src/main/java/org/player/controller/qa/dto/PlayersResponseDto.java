package org.player.controller.qa.dto;

import java.util.List;

public class PlayersResponseDto {

    List<PlayerItemResponseDto> players;

    public List<PlayerItemResponseDto> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerItemResponseDto> players) {
        this.players = players;
    }

    @Override
    public String toString() {
        return "Players{" +
                "players=" + players +
                '}';
    }
}

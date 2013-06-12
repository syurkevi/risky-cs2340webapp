package edu.gatech.cs2340.lobby.model;

import edu.gatech.cs2340.player.model.Player;

public class Lobby {
    String title;
    Player[] players;

    public Lobby(String title) {
        this.title = title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String  getTitle() {
        return title;
    }
}

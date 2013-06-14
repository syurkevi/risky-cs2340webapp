package edu.gatech.cs2340.risky.model;

public class Game {

    public Lobby lobby;

    public Game(Lobby lobby) {
        this.lobby = lobby;
    }

    public Game(int lobbyId) {
        this(new Lobby("lobbyId#" + lobbyId));
        //this(Lobby.getById(lobbyId));
    }
}

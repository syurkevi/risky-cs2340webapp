package edu.gatech.cs2340.risky.models;

import java.awt.Color;

import edu.gatech.cs2340.risky.Model;

public class Player extends Model {
    
    private static int[] colors = {Color.blue.getRGB(), Color.red.getRGB(), Color.green.getRGB(), Color.yellow.getRGB(), Color.black.getRGB(), Color.lightGray.getRGB()};
    private static int instanceCount = 0;
    public String name;
    public int armies;
    public String color;
    public boolean playing = true;
    
    public Player() {
        this(null);
    }
    
    public Player(String name, int armies) {
        this.name = name;
        this.armies = armies;
        this.playing = true;
        this.id = instanceCount;
        this.color = String.format("#%06X", (0xFFFFFF & this.colors[instanceCount%colors.length]));;
        instanceCount++;
    }
    
    public Player(String name) {
        this(name, 0);
    }
    
    public void setDead() {
        playing = false;
    }

    public boolean stillAlive() {
        return playing;
    }
    
    public void populateValidWith(Player p) {
        /*
        extract the value from p, and validate them, then store them in this instance
        if any values of p are invalid
        1. have a boolean option to either ignore the values, or throw an exception
        2. just throw an exception
        */
        this.name = p.name;
        this.armies = p.armies;
        this.playing = p.playing;
    }
}

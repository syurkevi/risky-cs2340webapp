package edu.gatech.cs2340.risky.api;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import edu.gatech.cs2340.risky.models.Lobby;

public class LobbyAdapter implements JsonSerializer<Lobby> {
    @Override
    public JsonElement serialize(Lobby src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        
        obj.addProperty("title", src.title);
        obj.add("players", new Gson().toJsonTree(src.getPlayers()));
        obj.addProperty("turnOrder", new Gson().toJson(src.turnOrder));
        obj.addProperty("mapId", src.mapId.toString());
        
        return obj;
    }
}
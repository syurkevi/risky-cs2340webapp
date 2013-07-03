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
        Gson defaultGson = new Gson();
        JsonObject obj = new JsonObject();
        
        obj.addProperty("id", src.id.toString());
        obj.addProperty("title", src.title);
        obj.add("players", defaultGson.toJsonTree(src.getPlayers()));
        obj.addProperty("mapId", src.mapId.toString());
        obj.add("turnOrder", defaultGson.toJsonTree(src.turnOrder));
        
        return obj;
    }
}
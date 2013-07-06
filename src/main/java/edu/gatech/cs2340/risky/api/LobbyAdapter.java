package edu.gatech.cs2340.risky.api;


public class LobbyAdapter /*implements JsonSerializer<Lobby>*/ {/*
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
    }*/
}
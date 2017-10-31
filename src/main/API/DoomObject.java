package main.API;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

class DoomObject
{
    long id;
    int typeId;

    double distToPlayer;
    String type;

    // Position info
    int angle;
    double x;
    double y;

    DoomObject(String inputJSON)
    {
        JSONParser parser = new JSONParser();
        JSONObject info = null;
        try
        {
            info = (JSONObject) parser.parse(inputJSON);
        } catch (ParseException e)
        {
            e.printStackTrace();
        }

        // Coords and player info
        JSONObject coords = (JSONObject) info.get("position");
        x = Double.parseDouble(coords.get("x").toString());
        y = Double.parseDouble(coords.get("y").toString());

        angle = Integer.parseInt(info.get("angle").toString());

        id = (long) info.get("id");
        typeId = Integer.parseInt(info.get("typeId").toString());

        distToPlayer = Double.parseDouble(info.get("distance").toString());
        type = (String) info.get("type");
    }
}

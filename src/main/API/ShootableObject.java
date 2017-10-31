package main.API;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

class ShootableObject extends DoomObject
{
    int health;

    ShootableObject(String inputJSON)
    {
        super(inputJSON);
        JSONParser parser = new JSONParser();
        JSONObject info = null;

        try
        {
            info = (JSONObject) parser.parse(inputJSON);
        } catch (ParseException e)
        {
            e.printStackTrace();
        }
        health = Integer.parseInt(info.get("health").toString());
    }

    boolean isInLos()
    {
        if (DoomAPI.clockTicks == 0)
            updateLos();

        Object isInLos = DoomAPI.idLos.get(id);
        return isInLos != null && (boolean) isInLos;
    }

    private void updateLos()
    {
        boolean isInLOS = DoomAPI.checkLos(DoomAPI.player.id, this.id);
        DoomAPI.idLos.put(id, isInLOS);
    }
}

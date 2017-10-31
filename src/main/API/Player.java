package main.API;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.geom.Point2D;

class Player
{
    long id = -1;

    // World properties
    boolean isMoving = false;
    boolean isTurning = false;

    boolean isShooting = false;
    ShootableObject target = null;

    // Stats
    int kills = 0;
    int weapon = 0;
    int secrets = 0;
    int items = 0;

    // Position info
    int angle = 0;
    double x = 0;
    double y = 0;
    double z = 0;

    // Weapons obtained
    Boolean hasHandgun = true;
    Boolean hasShotgun = false;
    Boolean hasChaingun = false;
    Boolean hasRocketLauncher = false;
    Boolean hasPlasmaRifle = false;
    Boolean hasBFG = false;

    // Keycards obtained
    Boolean redKeycard = false;
    Boolean blueKeycard = false;
    Boolean yellowKeycard = false;

    // Ammo values
    int bullets = 0;
    int shells = 0;
    int cells = 0;
    int rockets = 0;

    // Player info
    int health = 100;
    int armour = 0;

    void update(String inputJSON)
    {
        //System.out.println(inputJSON);
        JSONParser parser = new JSONParser();
        JSONObject info = null;

        try
        {
            info = (JSONObject) parser.parse(inputJSON);
        } catch (ParseException e)
        {
            e.printStackTrace();
        }

        long id = (long) info.get("id");
        this.id = id;

        // Coords and player info
        JSONObject coords = (JSONObject) info.get("position");
        x = Double.parseDouble(coords.get("x").toString());
        y = Double.parseDouble(coords.get("y").toString());

        angle = Integer.parseInt(info.get("angle").toString());

        health = Integer.parseInt(info.get("health").toString());
        armour = Integer.parseInt(info.get("armor").toString());

        items = Integer.parseInt(info.get("items").toString());
        secrets = Integer.parseInt(info.get("secrets").toString());

        // Weapons
        weapon = Integer.parseInt(info.get("weapon").toString());

        JSONObject weapons = (JSONObject) info.get("weapons");
        hasHandgun = (Boolean) weapons.get("Handgun");
        hasShotgun = (Boolean) weapons.get("Shotgun");
        hasChaingun = (Boolean) weapons.get("Chaingun");
        hasRocketLauncher = (Boolean) weapons.get("Rocket Launcher");
        hasPlasmaRifle = (Boolean) weapons.get("Plasma Rifle");
        hasBFG = (Boolean) weapons.get("BFG");

        // Ammo
        JSONObject ammo = (JSONObject) info.get("ammo");
        bullets = Integer.parseInt(ammo.get("Bullets").toString());
        shells = Integer.parseInt(ammo.get("Shells").toString());
        cells = Integer.parseInt(ammo.get("Cells").toString());
        rockets = Integer.parseInt(ammo.get("Rockets").toString());

        // Keycards
        JSONObject keycards = (JSONObject) info.get("keyCards");
        redKeycard = (Boolean) keycards.get("red");
        blueKeycard = (Boolean) keycards.get("blue");
        yellowKeycard = (Boolean) keycards.get("yellow");
    }

    double getX()
    {
        return x;
    }

    double getY()
    {
        return y;
    }

    int getAngle(Point2D target)
    {
        double diffX = target.getX() - x;
        double diffY = target.getY() - y;
        int angle = (int) Math.toDegrees(Math.atan2(diffY, diffX));
        if (angle < 0)
            angle += 360;

        return angle;
    }

    boolean lookingAt(Point2D target)
    {
        double distance = Math.hypot(x-target.getX(), y-target.getY());
        int requiredAngle = getAngle(target);

        int tolerance = 2;
        if(distance < 25)
            tolerance = 35;
        if(distance < 10)
            tolerance = 45;

        return Math.abs((angle - requiredAngle)) < tolerance;
    }

    boolean isPositionedAt(Point2D point)
    {
        double diffX = Math.abs(point.getX() - x);
        double diffY = Math.abs(point.getY() - y);
        return diffX < 100 && diffY < 100;
    }

    ShootableObject getTarget()
    {
        ShootableObject returnTarget = null;
        for (ShootableObject target : DoomAPI.shootableObjects)
        {
            if (target.id != this.id && target.health > 0 &&
                    (returnTarget == null || target.distToPlayer < returnTarget.distToPlayer) && target.isInLos() && target.distToPlayer < 800) // target.health < returnTarget.health
            {
                    returnTarget = target;
            }

        }
        return returnTarget;
    }
}

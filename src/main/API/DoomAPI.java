package main.API;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

public class DoomAPI
{
    static Player player = new Player();
    static ArrayList<ShootableObject> shootableObjects = new ArrayList<ShootableObject>();
    static ArrayList<DoomObject> objects = new ArrayList<DoomObject>();
    static ArrayList<Door> doors = new ArrayList<Door>();
    static HashMap<Long, Boolean> idLos = new HashMap<>();

    static int clockTicks = 0;

    private static void getObjectArray(String inputJSON)
    {
        JSONParser parser = new JSONParser();
        JSONArray info = null;

        try
        {
            info = (JSONArray) parser.parse(inputJSON);
        } catch (ParseException e)
        {
            e.printStackTrace();
        }

        objects.clear();
        shootableObjects.clear();

        for (Object obj : info)
        {
            JSONObject jObj = (JSONObject) obj;
            //System.out.println(jObj.toJSONString());

            if (isEnemy(jObj.get("flags").toString()))
                shootableObjects.add(new ShootableObject(jObj.toJSONString()));
            else
                objects.add(new DoomObject(jObj.toJSONString()));
        }
    }

    static Boolean turnLeft(double amount)
    {

        amount /= 1;
        //System.out.println(amount);

        String response = request("/api/player/actions", "{\"type\": \"turn-left\",\"amount\": " + ((int) amount) + "}",
                "POST");
        if (response == null)
            return false;
        return true;
    }

    static Boolean turnRight(double amount)
    {

        String response = request("/api/player/actions", "{\"type\": \"turn-right\",\"amount\": " + (amount) + "}",
                "POST");

        if (response == null)
            return false;
        return true;

    }

    static Boolean goForward(double amount)
    {
        String response = request("/api/player/actions", "{\"type\": \"forward\",\"amount\": " + amount + "}", "POST");
        if (response == null)
            return false;
        return true;
    }

    public static Boolean goBackwards(double amount)
    {
        String response = request("/api/player/actions", "{\"type\": \"backwards\",\"amount\": " + amount + "}",
                "POST");
        if (response == null)
            return false;
        return true;

    }

    static Boolean shoot(double amount)
    {
        String response = request("/api/player/actions", "{\"type\": \"shoot\",\"amount\": " + amount + "}", "POST");
        if (response == null)
            return false;
        return true;
    }

    static Boolean use()
    {
        String response = request("/api/player/actions", "{\"type\": \"use\"}", "POST");
        if (response == null)
            return false;
        return true;
    }

    static Boolean strafeLeft(double amount)
    {
        String response = request("/api/player/actions", "{\"type\": \"strafe-left\",\"amount\": " + amount + "}",
                "POST");
        if (response == null)
            return false;
        return true;
    }

    static Boolean strafeRight(double amount)
    {
        String response = request("/api/player/actions", "{\"type\": \"strafe-right\",\"amount\": " + amount + "}",
                "POST");
        if (response == null)
            return false;
        return true;
    }

    public static Boolean switchWeapon(int amount)
    {
        String response = request("/api/player/actions", "{\"type\": \"strafe-right\",\"amount\": " + amount + "}",
                "POST");
        if (response == null)
            return false;
        return true;
    }

    static void updateDoors()
    {
        String response = get("/api/world/doors");
        JSONParser parser = new JSONParser();
        JSONArray info = null;
        try
        {
            info = (JSONArray) parser.parse(response);
        } catch (ParseException e)
        {
            e.printStackTrace();
        }

        doors.clear();

        for (Object door : info)
        {
            JSONObject doorObj = (JSONObject) door;
            Boolean isOpen = false;

            if (doorObj.get("state").toString().equals("open"))
                isOpen = true;

            JSONObject line = (JSONObject) doorObj.get("line");

            int x1 = Integer.parseInt(((JSONObject) line.get("v1")).get("x").toString());
            int y1 = Integer.parseInt(((JSONObject) line.get("v1")).get("y").toString());

            int x2 = Integer.parseInt(((JSONObject) line.get("v2")).get("x").toString());
            int y2 = Integer.parseInt(((JSONObject) line.get("v2")).get("y").toString());

            doors.add(new Door(isOpen, doorObj.get("keyRequired").toString(), x1, x2, y1, y2));
        }
    }

    static Boolean checkLos(long id1, long id2)
    {
        String response = get("/api/world/los/" + id1 + "/" + id2);
        if (response == null)
            return false;

        JSONParser parser = new JSONParser();
        JSONObject info = null;
        try
        {
            info = (JSONObject) parser.parse(response);
        } catch (ParseException e)
        {
            e.printStackTrace();
        }

        if ((Boolean) info.get("los"))
            return true;
        return false;
    }

    static Boolean updatePlayerInfo()
    {
        String response = get("/api/player");
        if (response == null)
            return false;

        player.update(response);
        return true;

    }

    static Boolean updateObjects()
    {
        String response = get("/api/world/objects");
        if (response == null)
            return false;

        getObjectArray(response);
        // System.out.println(response);
        return true;
    }

    private static Boolean isEnemy(String flags)
    {
        JSONParser parser = new JSONParser();
        JSONObject info;
        try
        {
            info = (JSONObject) parser.parse(flags);
        } catch (ParseException e)
        {
            e.printStackTrace();
            return false;
        }

        if (info.containsKey("MF_SHOOTABLE")) // && info.containsKey("MF_COUNTKILL")
        {
            if (Boolean.parseBoolean((info.get("MF_SHOOTABLE").toString()))) // && Boolean.parseBoolean(info.get("MF_COUNTKILL").toString()
                return true;
        }
        return false;
    }

    static Boolean turnAngle(double angle)
    {
        String response = request("/api/player/turn",
                "{  \"type\": \"" + "right" + "\",  \"target_angle\": " + angle + "}", "POST");
        if (response == null)
            return false;
        return true;
    }

    public static double angleBetweenTwoPoints(double x1, double y1, double x2, double y2)
    {
        double angle = (double) Math.toDegrees(Math.atan2(y2 - y1, x2 - x1));
        if (angle < 0)
            angle += 360;
        return angle;
    }

    private static String request(String command, String bodyText, String type)
    {
        HttpURLConnection connection = null;
        String targetURL = "http://localhost:6001" + command;

        try
        {
            // Create connection
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestMethod(type);
            connection.setRequestProperty("Content-Length", Integer.toString(bodyText.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setUseCaches(false);
            connection.setDoOutput(true);

            // Send request
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(bodyText);
            wr.close();

            // Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if
            // Java version 5+
            String line;
            while ((line = rd.readLine()) != null)
            {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        } finally
        {
            if (connection != null)
            {
                connection.disconnect();
            }
        }
    }

    private static String get(String command)
    {
        String output = "";
        try
        {
            URL yahoo = new URL("http://localhost:6001" + command);
            URLConnection yc = yahoo.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null)
                output += inputLine;
            in.close();
        } catch (Exception e)
        {
            System.out.println("ERROR: " + e);
            return null;
        }
        return output;
    }
}

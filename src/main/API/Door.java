package main.API;

public class Door
{
    private Boolean open;

    private Boolean red = false;
    private Boolean blue = false;
    private Boolean yellow = false;

    private int x1;
    private int y1;

    private int x2;
    private int y2;

    Door(Boolean open, String keyReqd, int x1, int x2, int y1, int y2)
    {
        this.open = open;
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;

        if (keyReqd.equals("blue"))
            blue = true;
        else if (keyReqd.equals("red"))
            red = true;
        else if (keyReqd.equals("yellow"))
            yellow = true;
    }

    public Boolean isOpen()
    {
        return open;
    }

    public int getX1()
    {
        return x1;
    }

    public int getX2()
    {
        return x2;
    }

    public int getY1()
    {
        return y1;
    }

    public int getY2()
    {
        return y2;
    }

    public Boolean isRed()
    {
        return red;
    }

    public Boolean isBlue()
    {
        return blue;
    }

    public Boolean isYellow()
    {
        return yellow;
    }
}

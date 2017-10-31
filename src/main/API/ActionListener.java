package main.API;

class ActionListener
{

    private Player lastPlayerData;
    boolean listenMove, listenTurn, listenHurt;

    ActionListener()
    {
        lastPlayerData = DoomAPI.player;
    }

    void tick()
    {
        if (listenMove)
        {
            DoomAPI.player.isMoving = hasMoved();
        }
        if (listenTurn)
        {
            DoomAPI.player.isTurning = hasTurned();
        }
        if (listenHurt && hasHurt())
        {
            // TODO
        }

        lastPlayerData = DoomAPI.player;
    }

    private boolean hasHurt()
    {
        if (lastPlayerData.health != DoomAPI.player.health)
            return true;
        return false;
    }

    private boolean hasMoved()
    {
        if (lastPlayerData.x != DoomAPI.player.x || lastPlayerData.y != DoomAPI.player.y)
            return true;
        return false;
    }

    private boolean hasTurned()
    {
        if (lastPlayerData.angle != DoomAPI.player.angle)
            return true;
        return false;
    }
}
package main.API;

import main.*;
import main.Main.AStarDoom.DoomMap;
import main.Main.AStarDoom.UnitMover;
import main.Main.Utils;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Path;
import scala.collection.immutable.List;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class MainAutobot
{
    private static DoomMap doomMap;

    public static void main(String[] args) throws InterruptedException
    {
        ActionListener actionListener = new ActionListener();

        Path path = getPathToVictory();
        int currentStepIndex = 0;

        actionListener.listenMove = true;
        actionListener.listenTurn = true;

        while (true)
        {
            if(currentStepIndex > path.getLength()-1)
                break;

            System.out.println("Ticking");
            update();
            actionListener.tick();

            Path.Step step = path.getStep(currentStepIndex);
            Point2D stepPoint = new Point2D.Double(step.getX(), step.getY());
            stepPoint = doomMap.actualCoordinates(stepPoint);

            if(DoomAPI.clockTicks % 30 == 0)
                DoomAPI.use();

            if (DoomAPI.player.isTurning || DoomAPI.player.isMoving)
            {
                System.out.println("In action, waiting");
                continue;
            }

            if(DoomAPI.player.isShooting)
            {
                Point2D targetPoint = new Point2D.Double(DoomAPI.player.target.x, DoomAPI.player.target.y);
                if(!DoomAPI.player.lookingAt(targetPoint))
                {
                    System.out.println("Turning");
                    DoomAPI.turnAngle(DoomAPI.player.getAngle(targetPoint)); // Doom BUGS
                    continue;
                }

                if(DoomAPI.clockTicks % 3 == 0)
                    DoomAPI.shoot(1);
            }
            else
            {
                if (!DoomAPI.player.isPositionedAt(stepPoint))
                {
                    moveTo(stepPoint);
                    continue;
                }

                System.out.println("Stepping to next point");
                currentStepIndex++;
            }
        }

        Thread.sleep(1500);
        DoomAPI.turnAngle(159);
        Thread.sleep(1000);
        DoomAPI.use();
    }

    private static void updateTarget()
    {
        if(DoomAPI.player.bullets < 1)
        {
            DoomAPI.player.isShooting = false;
            DoomAPI.player.target = null;
            return;
        }

        ShootableObject target = DoomAPI.player.getTarget();
        if (target == null)
        {
            DoomAPI.player.isShooting = false;
            DoomAPI.player.target = null;

        }
        else
        {
            DoomAPI.player.isShooting = true;
            DoomAPI.player.target = target;
        }
    }

    private static void moveTo(Point2D stepPoint)
    {
        System.out.println("Going forward");
        DoomAPI.goForward(15);

        if (!DoomAPI.player.lookingAt(stepPoint))
        {
            System.out.println("Turning");
            DoomAPI.turnAngle(DoomAPI.player.getAngle(stepPoint));
        }
    }

    private static Path getPathToVictory()
    {
        Wad wad = WadParser.createWad("/home/user/doom1.wad");
        Level level1 = wad.levels().iterator().next();
        List<WadLine> lines = level1.lines().iterator().next();

        java.util.List<Line2D> goodLines = Utils.getLines(lines, true);
        java.util.List<Point2D> obstacles = Utils.getObstacles(level1);

        Vertex playerStart = level1.playerStart().iterator().next();
        Point2D startCoords = new Point2D.Double(playerStart.x(), playerStart.y());

        doomMap = new DoomMap(goodLines, obstacles);
        startCoords = doomMap.normalizeCoordinates(startCoords);

        AStarPathFinder finder = new AStarPathFinder(doomMap, 1000, false);

        Point2D goalCoords = new Point2D.Double(2951, -4762);
        goalCoords = doomMap.normalizeCoordinates(goalCoords);

        Path path = finder.findPath(new UnitMover(), (int) startCoords.getX(), (int) startCoords.getY(), (int) goalCoords.getX(), (int) goalCoords.getY());
        return path;
    }

    private static void update()
    {
        updateTickClock();
        DoomAPI.updateDoors();
        DoomAPI.updateObjects();
        DoomAPI.updatePlayerInfo();
        updateTarget();
    }

    private static void updateTickClock()
    {
        if(DoomAPI.clockTicks >= 7)
            DoomAPI.clockTicks = 0;
        else
            DoomAPI.clockTicks++;
    }
}

package main.Main.AStarDoom;

import main.*;
import main.Main.Utils;
import org.junit.jupiter.api.Test;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Path;
import scala.collection.immutable.List;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class AStarTest
{
    private static List<WadLine> _lines;

    @Test
    public static void Simulate()
    {
        Wad wad = WadParser.createWad("C:/Users/Admin/Desktop/doom1.wad");
        Level level1 = wad.levels().iterator().next();
        _lines = level1.lines().iterator().next();
        java.util.List<Line2D> goodLines = Utils.getLines(_lines, true);

        Vertex playerStart = level1.playerStart().iterator().next();

        java.util.List<Line2D> wallLines = new ArrayList<>();
        wallLines.add(new Line2D.Double(-2,6,-4,6));
        wallLines.add(new Line2D.Double(-2,6,-2,2));
        wallLines.add(new Line2D.Double(-2,2,4,2));
        wallLines.add(new Line2D.Double(4,2,4,-4));
        wallLines.add(new Line2D.Double(4,-4,-2,-4));
        wallLines.add(new Line2D.Double(-2,-4,-2,0));
        wallLines.add(new Line2D.Double(-2,0,-4, 0));
        wallLines.add(new Line2D.Double(-4,0,-4, 6));
        wallLines.add(new Line2D.Double(0,-2,2,2));

        DoomMap map = new DoomMap(wallLines, null); //TODO: Add obstacles
        AStarPathFinder finder = new AStarPathFinder(map, 500, true);
        Point2D startCoords = new Point2D.Double(-3, 5);
        Point2D goalCoords = new Point2D.Double(3,1);
        startCoords = map.normalizeCoordinates(startCoords);
        goalCoords = map.normalizeCoordinates(goalCoords);
        Path path = finder.findPath(new UnitMover(), (int)startCoords.getX(), (int)startCoords.getY(), (int)goalCoords.getX(), (int)goalCoords.getY());
    }
}

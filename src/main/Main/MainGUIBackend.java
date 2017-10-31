package main.Main;

import main.*;
import javafx.scene.paint.Color;
import main.Main.AStarDoom.DoomMap;
import main.Main.AStarDoom.UnitMover;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Path;
import scala.collection.immutable.List;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.IOException;

class MainGUIBackend
{
    static void initialize() throws IOException
    {
        Wad wad = WadParser.createWad("C:/Users/Admin/Desktop/doom1.wad");
        Level level1 = wad.levels().iterator().next();
        List<WadLine> lines = level1.lines().iterator().next();

        java.util.List<Line2D> goodLines = Utils.getLines(lines, true);
        java.util.List<Point2D> obstacles = Utils.getObstacles(level1);
        DoomMap map = new DoomMap(goodLines, obstacles);

        Vertex playerStart = level1.playerStart().iterator().next();
        Point2D startCoords = map.normalizeCoordinates(new Point2D.Double(playerStart.x(), playerStart.y()));
        AStarPathFinder finder = new AStarPathFinder(map, 1000, true);
        Point2D goalCoords = map.normalizeCoordinates(new Point2D.Double(2951, -4762));

        Path path = finder.findPath(new UnitMover(), (int) startCoords.getX(), (int) startCoords.getY(), (int) goalCoords.getX(), (int) goalCoords.getY());
        if (path == null)
            return;

        Path.Step firstStep = path.getStep(0);
        Point2D previousPoint = new Point2D.Double(firstStep.getX(), firstStep.getY());
        for (int i = 1; i < path.getLength(); i++)
        {
            Path.Step step = path.getStep(i);
            Point2D currentPoint = new Point2D.Double(step.getX(), step.getY());
            MainGUI.drawLine(new Line2D.Double(previousPoint, currentPoint), Color.BLUE);
            previousPoint = currentPoint;
        }

        MainGUI.markNode((int) startCoords.getX(), (int) startCoords.getY(), Color.AQUA);
        MainGUI.markNode((int) goalCoords.getX(), (int) goalCoords.getY(), Color.AQUA);
    }
}

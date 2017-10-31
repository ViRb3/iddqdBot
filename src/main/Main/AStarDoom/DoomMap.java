package main.Main.AStarDoom;

import javafx.scene.paint.Color;
import main.Main.MainGUI;
import org.newdawn.slick.util.pathfinding.Mover;
import org.newdawn.slick.util.pathfinding.TileBasedMap;
import sun.applet.Main;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.List;

public class DoomMap implements TileBasedMap
{
    private final List<Line2D> wallLines;
    private final List<Point2D> obstacles;

    private Rectangle bounds = new Rectangle();
    private Point2D translation = new Point2D.Double(0, 0);
    private int scaleDown = 25;

    public DoomMap(List<Line2D> wallLines, List<Point2D> obstacles)
    {
        this.wallLines = wallLines;
        this.obstacles = obstacles;
        readMapBounds();
        translateWallLines();

        for (Line2D line : wallLines)
            MainGUI.drawLine(line, javafx.scene.paint.Color.RED);

        for (Point2D obstacle : obstacles)
        {
            Point2D normalizedObstacle = normalizeCoordinates(obstacle);
            MainGUI.markNode((int) normalizedObstacle.getX(), (int) normalizedObstacle.getY(), Color.GREEN);
        }
    }

    private void translateWallLines()
    {
        for (Line2D line : wallLines)
            normalizeLine(line);
    }

    private void normalizeLine(Line2D line)
    {
        Point2D p1 = normalizeCoordinates(line.getP1());
        Point2D p2 = normalizeCoordinates(line.getP2());
        line.setLine(p1, p2);
    }

    private void readMapBounds()
    {
        Line2D firstLine = wallLines.get(0);
        int minX = (int) firstLine.getP1().getX(),
                maxX = (int) firstLine.getP1().getX(),
                minY = (int) firstLine.getP1().getX(),
                maxY = (int) firstLine.getP1().getX();

        for (Line2D line : wallLines)
        {
            int p1x = (int) line.getP1().getX();
            if (p1x < minX)
                minX = p1x;
            if (p1x > maxX)
                maxX = p1x;

            int p1y = (int) line.getP1().getY();
            if (p1y < minY)
                minY = p1y;
            if (p1y > maxY)
                maxY = p1y;

            int p2x = (int) line.getP2().getX();
            if (p2x < minX)
                minX = p2x;
            if (p2x > maxX)
                maxX = p2x;

            int p2y = (int) line.getP2().getY();
            if (p2y < minY)
                minY = p2y;
            if (p2y > maxY)
                maxY = p2y;
        }

        bounds = new Rectangle(minX, minY, maxX - minX, maxY - minY);
        if (bounds.x < 0)
        {
            translation.setLocation(Math.abs(bounds.x), translation.getY());
            bounds.x = 0;
        }
        if (bounds.y < 0)
        {
            translation.setLocation(translation.getX(), Math.abs(bounds.y));
            bounds.y = 0;
        }

        bounds.height /= scaleDown;
        bounds.width /= scaleDown;
    }

    //TODO: Make sure this method is called after 'translation' is set
    // convert actual coordinates to normalized (positive) coordinates
    public Point2D normalizeCoordinates(Point2D actualCoordinates)
    {
        Point2D translatedPt = new Point2D.Double(translation.getX() + actualCoordinates.getX(),
                translation.getY() + actualCoordinates.getY());
        return new Point2D.Double(translatedPt.getX() / scaleDown, translatedPt.getY() / scaleDown);
    }

    // convert normalized (positive) coordinates to actual coordinates
    public Point2D actualCoordinates(Point2D normalizedCoordinates)
    {
        Point2D unscaledPt = new Point2D.Double(normalizedCoordinates.getX() * scaleDown,
                normalizedCoordinates.getY() * scaleDown);
        return new Point2D.Double(unscaledPt.getX() - translation.getX(), unscaledPt.getY() - translation.getY());
    }

    @Override
    public int getWidthInTiles()
    {
        return bounds.width;
    }

    @Override
    public int getHeightInTiles()
    {
        return bounds.height;
    }

    @Override
    public void pathFinderVisited(int x, int y)
    {
    }

    private final double checkRadius = 0.75;

    @Override
    public boolean blocked(Mover mover, int cx, int cy, int x, int y)
    {
        Line2D movementLine = new Line2D.Double(cx, cy, x, y);

        Line2D[] proximityLines = new Line2D[] {
                new Line2D.Double(x, y, x-checkRadius, y),
                new Line2D.Double(x, y, x+checkRadius, y),
                new Line2D.Double(x, y, x, y-checkRadius),
                new Line2D.Double(x, y, x, y+checkRadius),
        };

        for (Line2D wallLine : wallLines)
        {
            if (movementLine.intersectsLine(wallLine))
                return true;
            for (Line2D proximityLine : proximityLines)
                if(proximityLine.intersectsLine(wallLine))
                {
                    MainGUI.drawLine(proximityLine, Color.YELLOW);
                    return true;
                }
        }

        return false;
    }

    @Override
    public float getCost(Mover mover, int sx, int sy, int tx, int ty)
    {
        return 1;
    }
}

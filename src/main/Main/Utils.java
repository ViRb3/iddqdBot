package main.Main;

import main.Level;
import main.Thing;
import main.WadLine;
import scala.collection.immutable.List;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Utils
{
    public static java.util.List<Line2D> getLines(List<WadLine> fuckedLines, boolean onesidedOnly)
    {
        java.util.List<Line2D> goodLines = new ArrayList<>();
        scala.collection.Iterator iter = fuckedLines.iterator();
        while (iter.hasNext())
        {
            WadLine current = (WadLine) iter.next();
            if (!onesidedOnly || current.oneSided())
                goodLines.add(new Line2D.Double(current.a().x(), current.a().y(), current.b().x(), current.b().y()));
        }
        return goodLines;
    }

    public static java.util.List<Point2D> getObstacles(Level level)
    {
        java.util.List<Point2D> obstacles = new ArrayList<>();
        scala.collection.Iterator iter = level.things().iterator();
        while (iter.hasNext())
        {
            Thing current = (Thing) iter.next();
            if(current.doomId() == 2035)
                obstacles.add(new Point2D.Double(current.position().x(), current.position().y()));
        }
        return obstacles;
    }
}
package main.Main;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.geom.Line2D;
import java.io.IOException;

public class MainGUI extends Application
{
    private static GraphicsContext _gc;

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException
    {
        primaryStage.setTitle("d00M b07");
        Group root = new Group();
        Canvas canvas = new Canvas(1024, 1024);
        _gc = canvas.getGraphicsContext2D();
        //drawGrid();
        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        MainGUIBackend.initialize();
    }

    private static int scale = 5;
    private static int ovalSize = 10;
    private static int strokeSize = 3;

    private void drawGrid() {
        for(int x = 0; x < 1000; x+= scale)
        {
            for (int y = 0; y < 1000; y += scale)
            {
                _gc.setFill(Color.GREEN);
                _gc.setStroke(Color.BLUE);
                _gc.fillOval(x-strokeSize/2, y-strokeSize/2, strokeSize, strokeSize);
            }
        }
    }

    public static void drawLine(Line2D line, Color color) {
        if(_gc == null)
            return;
        _gc.setStroke(color);
        _gc.setLineWidth(5);
        _gc.strokeLine(line.getX1()*scale, line.getY1()*scale, line.getX2()*scale, line.getY2()*scale);
    }

    public static void markNode(int x, int y, Color color)
    {
        if(_gc == null)
            return;
        _gc.setFill(color);
        _gc.fillOval(x*scale-ovalSize/2,y*scale-ovalSize/2, ovalSize, ovalSize);
    }
}

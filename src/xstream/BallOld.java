package xstream;

import java.util.Random;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


// This class cannot be saved using XStream!
// It relies on a JavaFX class to store most of its data.
public class BallOld implements Ball {
    private static Random ourGenerator = new Random();
    private Point2D myVelocity;
    private Circle myShape;

    
    public BallOld () {
        this(new Point2D(ourGenerator.nextInt(200), ourGenerator.nextInt(200)),
             ourGenerator.nextInt(16) + 15,
             new Point2D(ourGenerator.nextInt(5) - 3, ourGenerator.nextInt(5) - 3),
             new Color(ourGenerator.nextInt(256), ourGenerator.nextInt(256), ourGenerator.nextInt(256), 1));
    }

    public BallOld (Point2D center, int size, Point2D velocity, Color color) {
        myShape = new Circle(center.getX(), center.getY(), size);
        myShape.setFill(color);
        myVelocity = new Point2D(velocity.getX(), velocity.getY());
    }

    public void update (Dimension2D bounds) {
        myShape.setCenterX(myShape.getCenterX() + myVelocity.getX());
        myShape.setCenterY(myShape.getCenterY() + myVelocity.getY());
        if (myShape.getCenterX() >= bounds.getWidth() - myShape.getRadius() || 
            myShape.getCenterX() <= myShape.getRadius()) {
            myVelocity = new Point2D(myVelocity.getX() * -1, myVelocity.getY());
        }
        if (myShape.getCenterY() >= bounds.getHeight() - myShape.getRadius() || 
            myShape.getCenterY() <= myShape.getRadius()) {
            myVelocity = new Point2D(myVelocity.getX(), myVelocity.getY() * -1);
        }
    }

    public Node getView () {
        return myShape;
    }
}

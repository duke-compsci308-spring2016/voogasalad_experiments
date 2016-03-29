package xstream;

import java.util.Random;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


// This class can be saved using XStream.
// It stores all the information needed to create a view,
// i.e., JavaFX Node, but does not store the node itself.
public class BallNoNode implements Ball {
    private static Random ourGenerator = new Random();
    private Point2D myCenter;
    private Dimension2D mySize;
    private Point2D myVelocity;
    private Color myColor;


    public BallNoNode () {
        this(new Point2D(ourGenerator.nextInt(200), ourGenerator.nextInt(200)),
             ourGenerator.nextInt(16) + 15,
             new Point2D(ourGenerator.nextInt(5) - 3, ourGenerator.nextInt(5) - 3),
             new Color(ourGenerator.nextInt(256), ourGenerator.nextInt(256), ourGenerator.nextInt(256), 1));
    }

    public BallNoNode (Point2D center, int size, Point2D velocity, Color color) {
        myCenter = new Point2D(center.getX(), center.getY());
        mySize = new Dimension2D(size, size);
        myVelocity = new Point2D(velocity.getX(), velocity.getY());
        myColor = color;
    }

    public void update (Dimension2D bounds) {
        myCenter = myCenter.add(myVelocity);
        if (myCenter.getX() >= bounds.getWidth() - mySize.getWidth() || 
            myCenter.getX() <= mySize.getWidth()) {
            myVelocity = new Point2D(myVelocity.getX() * -1, myVelocity.getY());
        }
        if (myCenter.getY() >= bounds.getHeight() - mySize.getHeight() || 
            myCenter.getY() <= mySize.getHeight()) {
            myVelocity = new Point2D(myVelocity.getX(), myVelocity.getY() * -1);
        }
    }

    // Create a view each frame with the current state of the data.
    // NOTE, this requires getView to be called each frame (not standard)
    public Node getView () {
        Circle shape = new Circle(myCenter.getX(), myCenter.getY(), mySize.getWidth());
        shape.setFill(myColor);
        return shape;
    }
}

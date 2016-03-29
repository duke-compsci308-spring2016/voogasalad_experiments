package xstream;

import java.util.Random;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


// This class can be saved using XStream.
// It stores all the information needed to create a view,
// as well as the view itself, but it tells XStream NOT to
// save that data.  It then recreates it when loaded via XStream.
public class BallNotSaveNode implements Ball {
    private static Random ourGenerator = new Random();
    private Point2D myCenter;
    private int mySize;
    private Point2D myVelocity;
    private Color myColor;
    // do NOT save this field
    @XStreamOmitField
    private transient Circle myShape;


    public BallNotSaveNode () {
        init(new Point2D(ourGenerator.nextInt(200), ourGenerator.nextInt(200)),
             ourGenerator.nextInt(16) + 15,
             new Point2D(ourGenerator.nextInt(5) - 3, ourGenerator.nextInt(5) - 3),
             new Color(ourGenerator.nextInt(256), ourGenerator.nextInt(256), ourGenerator.nextInt(256), 1));
    }

    public BallNotSaveNode (Point2D center, int size, Point2D velocity, Color color) {
        init(center, size, velocity, color);
    }
    
    // Load the incomplete version, and then setup the calculated data as well
    public BallNotSaveNode (XStream serializer, String data) {
        BallNotSaveNode incomplete = (BallNotSaveNode)serializer.fromXML(data);
        init(incomplete.myCenter, incomplete.mySize, incomplete.myVelocity, incomplete.myColor);
    }

    // Update both representations here
    public void update (Dimension2D bounds) {
        myCenter = myCenter.add(myVelocity);
        myShape.setCenterX(myCenter.getX());
        myShape.setCenterY(myCenter.getY());
        if (myCenter.getX() >= bounds.getWidth() - mySize || 
            myCenter.getX() <= mySize) {
            myVelocity = new Point2D(myVelocity.getX() * -1, myVelocity.getY());
        }
        if (myCenter.getY() >= bounds.getHeight() - mySize || 
            myCenter.getY() <= mySize) {
            myVelocity = new Point2D(myVelocity.getX(), myVelocity.getY() * -1);
        }
    }

    // This method now only needs to be called once
    public Node getView () {
        return myShape;
    }

    // Shared initialization method
    private void init (Point2D center, int size, Point2D velocity, Color color) {
        myCenter = new Point2D(center.getX(), center.getY());
        myVelocity = new Point2D(velocity.getX(), velocity.getY());
        mySize = size;
        myColor = color;
        myShape = new Circle(center.getX(), center.getY(), size);
        myShape.setFill(color);
    }
}

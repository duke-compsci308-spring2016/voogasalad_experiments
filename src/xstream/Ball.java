package xstream;
import javafx.geometry.Dimension2D;
import javafx.scene.Node;


// The basic methods used in this demo.
public interface Ball {
    // change the attributes
    public void update (Dimension2D bounds);

    // get an updated view of the attributes
    public Node getView ();
}

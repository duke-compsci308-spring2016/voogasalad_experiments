package xstream;

import java.util.Random;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import javafx.animation.KeyFrame;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;


/**
 * This represents the primary class for a game/animation.
 *
 * @author Robert C. Duvall
 */
class BallWorld {
    private static final int OPPONENT_SIZE = 40;
    private static final int PLAYER_SPEED = 4;

    private Scene myScene;
    private Group myRoot;
    private ImageView myPlayer;
    private Ball myEnemy;
    private Random myGenerator = new Random();
    private XStream mySerializer = new XStream(new DomDriver());
    private String mySavedEnemy = "";


    /**
     * Create the game's scene
     */
    public Scene init (Stage s, int width, int height) {
        // create a scene graph to organize the scene
        myRoot = new Group();
        // make some shapes and set their properties
        myPlayer = new ImageView(new Image(getClass().getResourceAsStream("images/duke.gif")));
        myPlayer.setTranslateX(50 + myGenerator.nextInt(width - 100));
        myPlayer.setTranslateY(50 + myGenerator.nextInt(height - 100));
        myEnemy = new BallNoNode(new Point2D(myGenerator.nextInt(width), myGenerator.nextInt(height)),
                                      OPPONENT_SIZE,
                                      new Point2D(myGenerator.nextInt(5) - 3, myGenerator.nextInt(5) - 3),
                                      Color.RED);
        // remember shapes for viewing later
        myRoot.getChildren().add(myEnemy.getView());
        myRoot.getChildren().add(myPlayer);
        // create a place to display the shapes and react to input
        myScene = new Scene(myRoot, width, height, Color.WHITE);
        myScene.setOnKeyPressed(e -> handleKeyInput(e));
        return myScene;
    }

    /**
     * Create the game's frame
     */
    public KeyFrame start (int frameRate) {
        return new KeyFrame(Duration.millis(1000 / frameRate), e -> updateSprites());
    }

    /**
     * What to do each game frame
     *
     * Change the sprite properties each frame by a tiny amount to animate them
     *
     * Note, there are more sophisticated ways to animate shapes, but these simple ways work too.
     */
    private void updateSprites () {
        myPlayer.setRotate(myPlayer.getRotate() + 1);
        myEnemy.update(new Dimension2D(myScene.getWidth(), myScene.getHeight()));
        // note, this may need to be added to refresh the view every frame or via observation
        myRoot.getChildren().set(0, myEnemy.getView());
    }

    /**
     * What to do each time a key is pressed
     */
    private void handleKeyInput (KeyEvent e) {
        KeyCode keyCode = e.getCode();
        if (keyCode == KeyCode.RIGHT) {
            myPlayer.setTranslateX(myPlayer.getTranslateX() + PLAYER_SPEED);
        }
        else if (keyCode == KeyCode.LEFT) {
            myPlayer.setTranslateX(myPlayer.getTranslateX() - PLAYER_SPEED);
        }
        else if (keyCode == KeyCode.UP) {
            myPlayer.setTranslateY(myPlayer.getTranslateY() - PLAYER_SPEED);
        }
        else if (keyCode == KeyCode.DOWN) {
            myPlayer.setTranslateY(myPlayer.getTranslateY() + PLAYER_SPEED);
        }
        else if (keyCode == KeyCode.S) {
            try {
                System.out.println("Saving ...");
                mySavedEnemy = mySerializer.toXML(myEnemy);
                System.out.println(mySavedEnemy);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        else if (keyCode == KeyCode.L) {
            try {
                System.out.println("Loading ...");
                //myEnemy = new BallNotSaveNode(mySerializer, mySavedEnemy);
                // the more standard way that does not require "sharing" the serializer
                myEnemy = (Ball)mySerializer.fromXML(mySavedEnemy);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}

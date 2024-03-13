//b2220765008
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ArrayList;
/**
 * Has the scale and volume parameters for updating when necessary.
 * Contains the title media to be played at the title and background selection (menu) screens.
 */
public class DuckHunt extends Application {
    static Image backgroundImage = new Image("assets/welcome/1.png");
    static double width = backgroundImage.getWidth();
    static double height = backgroundImage.getHeight();
    private static final double SCALE = 3.0;
    public static double getScale() {
        return SCALE;
    }
    ImageView cursor;
    public static MediaPlayer titleMedia;
    public static final double VOLUME = 0.025;
    public static double getVolume() {
        return VOLUME;
    }
    public static ArrayList<ImageView> crosshairs = new ArrayList<>();
    public static ArrayList<String> backgroundPaths = new ArrayList<>();
    URL resource = getClass().getResource("assets/effects/Title.mp3");
    {
        assert resource != null;
        titleMedia = new MediaPlayer(new Media(resource.toString()));
    }
    /**
     * Adds all background paths to the list.
     * Adds all crosshairs as ImageView to the list.
     * Sets title and icon of the game.
     * Sends the scale parameter to the title screen class.
     * Shows the stage.
     *
     * @param    primaryStage
     */
    public void start(Stage primaryStage) {
        double scale = DuckHunt.getScale();
        double volume = DuckHunt.getVolume();
        for (int i = 1; i < 7; i++) {
            String path = "assets/background/" + i + ".png";
            backgroundPaths.add(path);
        }
        for (int i = 1; i < 8; i++) {
            String path = "assets/crosshair/" + i + ".png";
            cursor = new ImageView(path);
            cursor.setFitWidth(cursor.getImage().getWidth() * scale);
            cursor.setFitHeight(cursor.getImage().getHeight() * scale);
            cursor.setLayoutX(width*scale/2);
            cursor.setLayoutY(height*scale/2);
            crosshairs.add(cursor);
        }
        StackPane titleRoot = titleClass.titleSetter(scale, volume);
        Scene titleScene = new Scene(titleRoot, DuckHunt.width*scale, DuckHunt.height*scale);
        primaryStage.setScene(titleScene);
        titleClass titleScn = new titleClass();
        titleScn.start(primaryStage, titleScene, scale, volume);
        primaryStage.getIcons().add(new Image("assets/favicon/1.png"));
        primaryStage.setTitle("HUBBM Duck Hunt");
        primaryStage.show();
    }
    public static void main(String[] args){
        launch(args);
    }
}

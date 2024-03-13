import javafx.animation.*;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.util.Duration;
/**
 * Has the necessary methods to set the title screen and its keyboard interactions.
 */
public class titleClass {
    static Text beginningText;
    /**
     * Creates the nodes of the root: background, texts, mediaview.
     *
     * @return   StackPane to create the scene
     * @param    scale for scaling every node of the scene
     */
    public static StackPane titleSetter(double scale, double volume){
        String backgroundSize = String.format("%fpx %fpx", scale * DuckHunt.width, scale * DuckHunt.height);
        StackPane root = new StackPane();
        root.setStyle("-fx-background-image: url('assets/welcome/1.png');" + "-fx-background-size: " + backgroundSize
                + ";" + "-fx-background-repeat: no-repeat;");
        beginningText = new Text(0, 0, "PRESS ENTER TO START\nPRESS ESC TO EXIT");
        beginningText.setFont(Font.font("Arial", FontWeight.BOLD, 13*scale));
        beginningText.setFill(Color.ORANGE);
        beginningText.setTextAlignment(TextAlignment.CENTER);
        VBox vbox = new VBox();
        vbox.getChildren().add(beginningText);
        vbox.setPadding(new Insets(150 * scale, ((DuckHunt.width*scale)-vbox.getWidth())/2,
                30 * scale, ((DuckHunt.width*scale)-vbox.getWidth())/2));
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.5), evt -> vbox.setVisible(false)),
                new KeyFrame(Duration.seconds(1.2), evt -> vbox.setVisible(true)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        DuckHunt.titleMedia.setOnEndOfMedia(new Runnable() {
            public void run() {
                DuckHunt.titleMedia.seek(Duration.ZERO);
            }
        });
        DuckHunt.titleMedia.setVolume(volume);
        DuckHunt.titleMedia.play();
        MediaView media = new MediaView(DuckHunt.titleMedia);
        root.getChildren().addAll(vbox, media);
        return root;
    }
    /**
     * Enables the keyboard-user interactions, pressing enter opens the menu screen and esc exits the game.
     *
     * @param    primaryStage   to set or close the stage with related keyboard interactions
     * @param    titleScene     for keyboard interactions
     * @param    scale          for scaling every node of the scenes
     */
    public void start(Stage primaryStage, Scene titleScene, double scale, double volume){
        titleScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case ENTER:
                        StackPane menuRoot = menuClass.menuSetter(scale);
                        Scene menuScene = new Scene(menuRoot, DuckHunt.width*scale, DuckHunt.height*scale);
                        menuScene.setCursor(Cursor.DEFAULT);
                        primaryStage.setScene(menuScene);
                        menuClass menuSceneClass = new menuClass();
                        menuClass.menuIsOver=false;
                        menuSceneClass.start(primaryStage, scale, menuScene, menuRoot, volume);
                        break;
                    case ESCAPE:
                        primaryStage.close();
                        break;
                }
            }
        });
    }
}

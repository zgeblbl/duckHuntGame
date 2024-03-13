import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.media.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ArrayList;
/**
 * Has the necessary methods to set the menu scene and its keyboard interactions as well as changing the background
 * and crosshair.
 */
public class menuClass {
    static Text menuText;
    static ImageView menuCursor;
    static ImageView selectedCursor;
    static String selectedBackgroundPath;
    static ImageView foreground;
    static int backgroundNumber, cursorNumber = 0;
    static boolean backgroundChanged, cursorChanged = false;
    static MediaPlayer introMedia;
    public static boolean menuIsOver = false;
    URL introResource = getClass().getResource("assets/effects/Intro.mp3");
    {
        assert introResource != null;
        introMedia = new MediaPlayer(new Media(introResource.toString()));
    }
    static MediaView menuMediaView = new MediaView(introMedia);
    /**
     * Changes the background (not directly, returns the path for future operations).
     *
     * @return   String                  the newly set background's path
     * @param    backgroundPaths         list of selectable background paths
     * @param    new_backgroundNumber   index of the selected background path
     * @param    backgroundChanged      boolean for seeing if the right/left keys were pressed and the background was changed
     */
    public static String backgroundChanger(ArrayList<String> backgroundPaths, int new_backgroundNumber, boolean backgroundChanged) {
        if (backgroundChanged) {
            return backgroundPaths.get(new_backgroundNumber);
        }else{
            return backgroundPaths.get(0);
        }
    }
    /**
     * Changes the crosshair (not directly, returns the ImageView for future operations).
     *
     * @return   ImageView          the newly set cursor
     * @param    crosshairs         list of selectable cursors
     * @param    new_cursorNumber   index of the selected cursor
     * @param    cursorChanged      boolean for seeing if the up/down keys were pressed and the cursor was changed
     */
    public static ImageView cursorChanger(ArrayList<ImageView> crosshairs, int new_cursorNumber, boolean cursorChanged){
        if (cursorChanged){
            return crosshairs.get(new_cursorNumber);
        }else{
            return crosshairs.get(0);
        }
    }
    /**
     * Creates the nodes of the root: background, texts, mediaview.
     *
     * @return   StackPane to create the scene
     * @param    scale for scaling every node of the scene
     */
    public static StackPane menuSetter(double scale){
        String menuBackgroundSize = String.format("%fpx %fpx", scale * DuckHunt.width, scale * DuckHunt.height);
        String backgroundPath = "assets/background/1.png";
        StackPane menu = new StackPane();
        menu.setStyle("-fx-background-image: url('" + backgroundPath + "');" + "-fx-background-size: "
                + menuBackgroundSize + ";" + "-fx-background-repeat: no-repeat;");
        menuText = new Text(0, 0, "USE ARROW KEYS TO NAVIGATE\nPRESS ENTER TO START\nPRESS ESC TO EXIT");
        menuText.setFont(Font.font("Arial", FontWeight.BOLD, 7*scale));
        menuText.setFill(Color.ORANGE);
        menuText.setTextAlignment(TextAlignment.CENTER);
        VBox menuVBox = new VBox();
        menuVBox.getChildren().add(menuText);
        menuVBox.setPadding(new Insets(20 * scale, 50*scale, 30 * scale, 67*scale));
        menuCursor = new ImageView("assets/crosshair/1.png");
        menuCursor.setFitWidth(menuCursor.getImage().getWidth() * scale);
        menuCursor.setFitHeight(menuCursor.getImage().getHeight() * scale);
        menuCursor.setX(DuckHunt.width*scale/2);
        menuCursor.setY(DuckHunt.height*scale/2);
        menu.getChildren().addAll(menuVBox, menuCursor, menuMediaView);
        return menu;
    }
    /**
     * Enables the keyboard-user interactions, pressing enter starts the game, esc returns to title screen.
     * Up/down keys change the crosshair and right/left keys change the background.
     *
     * @param    primaryStage   to set a scene to the stage with related keyboard interactions
     * @param    scale          for scaling every node of the scenes
     * @param    menuScene      for keyboard interactions
     * @param    menu           for changing the background and crosshair
     */
    public void start(Stage primaryStage, double scale, Scene menuScene, StackPane menu, double volume){
        String menuBackgroundSize = String.format("%fpx %fpx", scale * DuckHunt.width, scale * DuckHunt.height);
        cursorChanged=false;
        backgroundChanged=false;
        cursorNumber=0;
        backgroundNumber=0;
        if (selectedCursor!=null){
            selectedCursor.setTranslateX(0);
            selectedCursor.setTranslateY(0);
            selectedCursor.toFront();
        }
        menuScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (!menuIsOver){
                    switch (event.getCode()) {
                        case ENTER:
                            menuIsOver = true;
                            selectedCursor = cursorChanger(DuckHunt.crosshairs, cursorNumber, cursorChanged);
                            selectedBackgroundPath = backgroundChanger(DuckHunt.backgroundPaths, backgroundNumber,
                                    backgroundChanged);
                            String path = "assets/foreground/" + (backgroundNumber+1) + ".png";
                            foreground = new ImageView(path);
                            foreground.setFitWidth(foreground.getImage().getWidth() * scale);
                            foreground.setFitHeight(foreground.getImage().getHeight() * scale);
                            DuckHunt.titleMedia.stop();
                            introMedia.setVolume(volume);
                            introMedia.play();
                            introMedia.setOnEndOfMedia(() -> {
                                StackPane root = gameSceneClass.gameSetter(scale);
                                Scene gameScene = new Scene
                                        (root, DuckHunt.width*scale, DuckHunt.height*scale);
                                primaryStage.setScene(gameScene);
                                gameSceneClass gameScene1 = new gameSceneClass();
                                gameScene1.start(primaryStage, gameScene, scale, root, volume);
                            });
                            break;
                        case UP:
                            if (cursorNumber == 6) {
                                menu.getChildren().remove(DuckHunt.crosshairs.get(6));
                                menu.getChildren().add(DuckHunt.crosshairs.get(0));
                                cursorNumber = 0;
                            } else {
                                menu.getChildren().remove(DuckHunt.crosshairs.get(cursorNumber));
                                menu.getChildren().add(DuckHunt.crosshairs.get(cursorNumber + 1));
                                cursorNumber++;
                            }
                            cursorChanged=true;
                            break;
                        case DOWN:
                            if (cursorNumber == 0) {
                                menu.getChildren().remove(DuckHunt.crosshairs.get(0));
                                menu.getChildren().add(DuckHunt.crosshairs.get(6));
                                cursorNumber = 6;
                            } else {
                                menu.getChildren().remove(DuckHunt.crosshairs.get(cursorNumber));
                                menu.getChildren().add(DuckHunt.crosshairs.get(cursorNumber - 1));
                                cursorNumber--;
                            }
                            cursorChanged=true;
                            break;
                        case RIGHT:
                            if (backgroundNumber == 5) {
                                String backgroundPath = DuckHunt.backgroundPaths.get(0);
                                menu.setStyle("-fx-background-image: url('" + backgroundPath + "');"
                                        + "-fx-background-size: " + menuBackgroundSize + ";"
                                        + "-fx-background-repeat: no-repeat;");
                                backgroundNumber = 0;
                            } else {
                                String backgroundPath = DuckHunt.backgroundPaths.get((backgroundNumber + 1));
                                menu.setStyle("-fx-background-image: url('" + backgroundPath + "');"
                                        + "-fx-background-size: " + menuBackgroundSize + ";"
                                        + "-fx-background-repeat: no-repeat;");
                                backgroundNumber++;
                            }
                            backgroundChanged = true;
                            break;
                        case LEFT:
                            if (backgroundNumber == 0) {
                                String backgroundPath = DuckHunt.backgroundPaths.get(5);
                                menu.setStyle("-fx-background-image: url('" + backgroundPath + "');"
                                        + "-fx-background-size: " + menuBackgroundSize + ";"
                                        + "-fx-background-repeat: no-repeat;");
                                backgroundNumber = 5;
                            } else {
                                String backgroundPath = DuckHunt.backgroundPaths.get((backgroundNumber - 1));
                                menu.setStyle("-fx-background-image: url('" + backgroundPath + "');"
                                        + "-fx-background-size: " + menuBackgroundSize + ";"
                                        + "-fx-background-repeat: no-repeat;");
                                backgroundNumber--;
                            }
                            backgroundChanged = true;
                            break;
                        case ESCAPE:
                            StackPane titleRoot = titleClass.titleSetter(scale, volume);
                            Scene titleScene = new Scene
                                    (titleRoot, DuckHunt.width*scale, DuckHunt.height*scale);
                            titleScene.setCursor(Cursor.DEFAULT);
                            primaryStage.setScene(titleScene);
                            titleClass titleScn = new titleClass();
                            titleScn.start(primaryStage, titleScene, scale, volume);
                            break;
                    }
                }
            }
        });
    }
}

import javafx.animation.*;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.media.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.net.URL;
import java.util.Objects;
/**
 * Has the necessary methods to set the first level scene and its keyboard/mouse interactions.
 *
 */
public class gameSceneClass {
    private static Text ammoText, levelText, gOverText1, gOverText2, youWin, youWin2;
    private static int duck=1, ammo = duck*3, level = 1;
    URL bulletResource = getClass().getResource("assets/effects/Gunshot.mp3");
    URL gOverResource = getClass().getResource("assets/effects/GameOver.mp3");
    URL fallingResource = getClass().getResource("assets/effects/DuckFalls.mp3");
    URL levelWonResource = getClass().getResource("assets/effects/LevelCompleted.mp3");
    private static MediaPlayer bulletMedia, gOverMedia, fallingMedia, levelWonMedia;
    private static boolean gameOver, gameWon, duckIsFlipped = false;
    private static ImageView duckImageView = new ImageView();
    private static Timeline flyingTimeline, fallingTimeline;
    {
        assert bulletResource != null;
        bulletMedia = new MediaPlayer(new Media(bulletResource.toString()));
        assert gOverResource != null;
        gOverMedia = new MediaPlayer(new Media(gOverResource.toString()));
        assert fallingResource != null;
        fallingMedia = new MediaPlayer(new Media(fallingResource.toString()));
        assert levelWonResource != null;
        levelWonMedia = new MediaPlayer(new Media(levelWonResource.toString()));
    }
    private static final MediaView bulletMediaView = new MediaView(bulletMedia);
    /**
     * Creates the nodes of the root: background, texts, ducks and their flying/reflecting animations, mediaview and
     * the foreground.
     *
     * @return   StackPane  to create the scene
     * @param    scale      for scaling every node of the scene
     */
    public static StackPane gameSetter(double scale){
        duck = 1;
        ammo = duck*3;
        String menuBackgroundSize = String.format("%fpx %fpx", scale * DuckHunt.width, scale * DuckHunt.height);
        StackPane game = new StackPane();
        game.setStyle("-fx-background-image: url('" + menuClass.selectedBackgroundPath + "');" + "-fx-background-size: "
                + menuBackgroundSize + ";" + "-fx-background-repeat: no-repeat;");
        ammoText = new Text(0, 0, "Ammo Left: " + ammo);
        ammoText.setFont(Font.font("Arial", FontWeight.BOLD, 7*scale));
        ammoText.setFill(Color.ORANGE);
        levelText = new Text(0, 0, "Level " + level + "/6");
        levelText.setFont(Font.font("Arial", FontWeight.BOLD, 7*scale));
        levelText.setFill(Color.ORANGE);
        VBox ammoVbox = new VBox();
        ammoVbox.getChildren().add(ammoText);
        ammoVbox.setPadding(new Insets(10 * scale, 33*scale, 30 * scale, 150 * scale));
        ammoVbox.setAlignment(Pos.TOP_RIGHT);
        VBox levelVbox = new VBox();
        levelVbox.getChildren().add(levelText);
        levelVbox.setPadding(new Insets(10 * scale, 50 * scale, 30 * scale, 50 * scale));
        levelVbox.setAlignment(Pos.TOP_CENTER);
        String[] duckImagesFlying = {"assets/duck_black/4.png", "assets/duck_black/5.png", "assets/duck_black/6.png"};
        Image duck = new Image("assets/duck_black/4.png");
        double duckWidth = duck.getWidth()*scale;
        double duckHeight = duck.getHeight()*scale;
        Image[] duckImages = new Image[duckImagesFlying.length];
        for (int i = 0; i < duckImagesFlying.length; i++) {
            duckImages[i] = new Image(Objects.requireNonNull
                    (gameSceneClass.class.getResourceAsStream(duckImagesFlying[i])));
        }
        duckImageView.setFitWidth(duckWidth);
        duckImageView.setFitHeight(duckHeight);
        duckImageView.setTranslateX(-duckWidth*scale);
        duckImageView.setTranslateY(-67 * scale);
        final int[] currentImageIndex = {0};
        flyingTimeline = new Timeline(new KeyFrame(Duration.millis(200), event -> {
            duckImageView.setImage(duckImages[currentImageIndex[0]]);
            currentImageIndex[0] = (currentImageIndex[0] + 1) % duckImagesFlying.length;
            double initialX = -duckWidth*scale;
            double finalX = duckWidth*scale;
            double unitMove = 8*scale;
            final double currentX = duckImageView.getTranslateX();
            double newX = currentX + unitMove;
            duckImageView.setScaleX(1);
            if (newX <= finalX && !duckIsFlipped) {
                duckImageView.setTranslateX(newX);
            }else{
                duckIsFlipped =true;
                newX = currentX - unitMove;
                duckImageView.setScaleX(-1);
                duckImageView.setTranslateX(newX);
                if (currentX<initialX){
                    duckImageView.setScaleX(1);
                    duckIsFlipped =false;
                    duckImageView.setTranslateX(initialX);
                }
            }
            })
        );
        flyingTimeline.setCycleCount(Animation.INDEFINITE);
        flyingTimeline.stop();
        flyingTimeline.play();
        if(fallingTimeline!=null){
            fallingTimeline.stop();
        }
        game.getChildren().clear();
        game.getChildren().addAll(bulletMediaView, ammoVbox, levelVbox, duckImageView, menuClass.foreground);
        gameWon = false;
        gameOver = false;
        duckIsFlipped=false;
        return game;
    }
    /**
     * Enables the keyboard/mouse interactions.
     * Left mouse button does the "shooting" action, crosshair takes place of the default cursor.
     * If a duck is shot, falling animation and other actions take place.
     * If level is won, pressing enter sets the second level scene and starts the second level.
     * If level is lost, pressing enter sets the first level scene and restarts the first level; pressing escape key
     * returns the player to the title scene.
     *
     * @param    primaryStage   to set a scene to the stage with related keyboard interactions
     * @param    gameScene      for keyboard/mouse interactions and the selected crosshair replacing the default cursor
     * @param    scale          for scaling every node of the scenes
     * @param    game           for adding and removing nodes in related cases
     */
    public void start(Stage primaryStage, Scene gameScene, double scale, StackPane game, double volume){
        VBox gOverVbox1 = new VBox(), gOverVbox2 = new VBox(), youWinVbox1 = new VBox(), youWinVbox2 = new VBox();
        gameScene.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                if (!gameOver && !gameWon && duck!=0) {
                    bulletMedia.setVolume(volume);
                    bulletMedia.stop();
                    bulletMedia.play();
                    ammo--;
                    ammoText.setText("Ammo Left: " + ammo);
                    double bulletX = event.getX();
                    double bulletY = event.getY();
                    Bounds boundsInParent = duckImageView.getBoundsInParent();
                    double xMin = boundsInParent.getMinX();
                    double xMax = boundsInParent.getMaxX();
                    double yMax = boundsInParent.getMaxY();
                    double yMin = boundsInParent.getMinY();
                    if (bulletX <= xMax && xMin <= bulletX && bulletY <= yMax && yMin <= bulletY){
                        duck--;
                        flyingTimeline.stop();
                        ImageView shotDuck = new ImageView("assets/duck_black/7.png");
                        ImageView fallingDuck = new ImageView("assets/duck_black/8.png");
                        if (duckIsFlipped){
                            shotDuck.setScaleX(-1);
                            fallingDuck.setScaleX(-1);
                        }
                        duckImageView.setImage(shotDuck.getImage());
                        Timeline shotImageChange = new Timeline(new KeyFrame(Duration.seconds(0.5), event2 -> {
                            duckImageView.setImage(fallingDuck.getImage());
                        }));
                        shotImageChange.play();
                        shotImageChange.setOnFinished(event1 -> {
                            fallingMedia.setVolume(volume);
                            fallingMedia.play();
                            fallingTimeline = new Timeline(new KeyFrame(Duration.seconds(0.2), event2 -> {
                                double unitMove = 8*scale;
                                final double currentY = duckImageView.getTranslateY();
                                double newY = currentY + unitMove;
                                duckImageView.setTranslateY(newY);
                            }));
                            fallingTimeline.setCycleCount(Animation.INDEFINITE);
                            fallingTimeline.play();
                            fallingTimeline.setOnFinished(event2 -> {
                                fallingMedia.stop();
                            });
                            if (duck==0){
                                gameWon = true;
                            }
                            if (gameWon){
                                youWin = new Text(0, 0, "YOU WIN!\n\n");
                                youWin.setFont(Font.font("Arial", FontWeight.BOLD, 10*scale));
                                youWin.setFill(Color.ORANGE);
                                youWin2 = new Text(0, 0, "\nPress ENTER to play next level");
                                youWin2.setFont(Font.font("Arial", FontWeight.BOLD, 10*scale));
                                youWin2.setFill(Color.ORANGE);
                                youWin2.setTextAlignment(TextAlignment.CENTER);
                                youWinVbox1.setAlignment(Pos.CENTER);
                                youWinVbox2.setAlignment(Pos.CENTER);
                                youWinVbox1.getChildren().addAll(youWin);
                                youWinVbox2.getChildren().addAll(youWin2);
                                Timeline flashingTimeline = new Timeline(
                                        new KeyFrame(Duration.seconds(0.5), evt -> youWin2.setVisible(true)),
                                        new KeyFrame(Duration.seconds(1.0), evt -> youWin2.setVisible(false))
                                );
                                flashingTimeline.setCycleCount(Animation.INDEFINITE);
                                flashingTimeline.play();
                                game.getChildren().addAll(youWinVbox1, youWinVbox2);
                                levelWonMedia.setVolume(volume);
                                levelWonMedia.play();
                            }
                        });
                    }
                }
                if (ammo == 0 && !gameWon && duck!=0) {
                    gameOver = true;
                    gOverVbox1.getChildren().clear();
                    gOverVbox2.getChildren().clear();
                    gOverText1 = new Text(0, 0, "GAME OVER!\n\n");
                    gOverText1.setFont(Font.font("Arial", FontWeight.BOLD, 10*scale));
                    gOverText1.setFill(Color.ORANGE);
                    gOverText2 = new Text(0, 0, "\nPress ENTER to play again\nPress ESC to exit");
                    gOverText2.setFont(Font.font("Arial", FontWeight.BOLD, 10*scale));
                    gOverText2.setFill(Color.ORANGE);
                    gOverText2.setTextAlignment(TextAlignment.CENTER);
                    gOverVbox1.setAlignment(Pos.CENTER);
                    gOverVbox2.setAlignment(Pos.CENTER);
                    gOverVbox1.getChildren().addAll(gOverText1);
                    gOverVbox2.getChildren().addAll(gOverText2);
                    Timeline flashingTimeline = new Timeline(
                            new KeyFrame(Duration.seconds(0.5), evt -> gOverText2.setVisible(true)),
                            new KeyFrame(Duration.seconds(1.0), evt -> gOverText2.setVisible(false))
                    );
                    flashingTimeline.setCycleCount(Animation.INDEFINITE);
                    flashingTimeline.play();
                    game.getChildren().removeAll(gOverVbox1, gOverVbox2);
                    game.getChildren().addAll(gOverVbox1, gOverVbox2);
                    gOverMedia.setVolume(volume);
                    gOverMedia.play();
                }
            }
        });
        gameScene.setOnMouseEntered(event -> {
            game.getChildren().add(menuClass.selectedCursor);
        });
        gameScene.setOnMouseMoved(event -> {
            menuClass.selectedCursor.setTranslateX(event.getX()-gameScene.getWidth()/2);
            menuClass.selectedCursor.setTranslateY(event.getY()-gameScene.getHeight()/2);
        });
        gameScene.setOnMouseExited(event -> {
            game.getChildren().remove(menuClass.selectedCursor);
        });
        gameScene.setCursor(Cursor.NONE);
        gameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (Objects.requireNonNull(event.getCode()) == KeyCode.ENTER) {
                    levelWonMedia.stop();
                    if (gameOver) {
                        gameOver = false;
                        ammo = 3;
                        ammoText.setText("Ammo Left: " + ammo);
                        game.getChildren().removeAll(gOverVbox1, gOverVbox2);
                        primaryStage.setScene(gameScene);
                        gameSceneClass gameScene1 = new gameSceneClass();
                        gameScene1.start(primaryStage, gameScene, scale, game, volume);
                    } else if (gameWon) {
                        fallingTimeline.stop();
                        StackPane root2 = levelTwoClass.levelTwoSetter(scale);
                        Scene levelTwoScene = new Scene
                                (root2, DuckHunt.width * scale, DuckHunt.height * scale);
                        levelTwoScene.setCursor(Cursor.DEFAULT);
                        primaryStage.setScene(levelTwoScene);
                        levelTwoClass gameScene2 = new levelTwoClass();
                        gameScene2.start(primaryStage, levelTwoScene, scale, root2, volume);
                    }
                }if (Objects.requireNonNull(event.getCode()) == KeyCode.ESCAPE) {
                    gOverMedia.stop();
                    if (gameOver) {
                        flyingTimeline.stop();
                        StackPane titleRoot = titleClass.titleSetter(scale, volume);
                        Scene titleScene = new Scene
                                (titleRoot, DuckHunt.width * scale, DuckHunt.height * scale);
                        titleScene.setCursor(Cursor.DEFAULT);
                        primaryStage.setScene(titleScene);
                        titleClass titleScn = new titleClass();
                        titleScn.start(primaryStage, titleScene, scale, volume);
                    }
                }
            }
        });
    }

}
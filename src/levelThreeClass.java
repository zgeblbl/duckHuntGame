import javafx.animation.*;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.net.URL;
import java.util.Objects;
/**
 * Has the necessary methods to set the third level scene and its keyboard/mouse interactions.
 *
 */
public class levelThreeClass {
    URL bulletResource = getClass().getResource("assets/effects/Gunshot.mp3");
    URL gOverResource = getClass().getResource("assets/effects/GameOver.mp3");
    URL fallingResource = getClass().getResource("assets/effects/DuckFalls.mp3");
    URL levelWonResource = getClass().getResource("assets/effects/LevelCompleted.mp3");
    private static MediaPlayer bulletMedia, gOverMedia, fallingMedia, levelWonMedia;
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
    static MediaView bulletMediaView = new MediaView(bulletMedia);
    private static Text ammoText, levelText, gOverText1, gOverText2, youWin, youWin2;
    private static ImageView blackDuckImageView = new ImageView(), blueDuckImageView = new ImageView();
    private static Timeline black_flyingTimeline = new Timeline(), blue_flyingTimeline = new Timeline();
    private static Timeline black_fallingTimeline = new Timeline(), blue_fallingTimeline = new Timeline();
    private static boolean gameOver, gameWon, black_duckIsFlipped_x, blue_duckIsFlipped_x = false;
    private static int duck=2, ammo = duck*3, level = 3;
    /**
     * Creates the nodes of the root: background, texts, ducks and their flying/reflecting animations, mediaview and
     * the foreground.
     *
     * @return   StackPane  to create the scene
     * @param    scale      for scaling every node of the scene
     */
    public static StackPane levelThreeSetter(double scale){
        duck = 2;
        ammo = duck*3;
        String menuBackgroundSize = String.format("%fpx %fpx", scale * DuckHunt.width, scale * DuckHunt.height);
        StackPane levelThree = new StackPane();
        levelThree.setStyle("-fx-background-image: url('" + menuClass.selectedBackgroundPath + "');"
                + "-fx-background-size: " + menuBackgroundSize + ";" + "-fx-background-repeat: no-repeat;");
        ammoText = new Text(0, 0, "Ammo Left: " + ammo);
        ammoText.setFont(Font.font("Arial", FontWeight.BOLD, 7*scale));
        ammoText.setFill(Color.ORANGE);
        levelText = new Text(0, 0, "Level " + level + "/6");
        levelText.setFont(Font.font("Arial", FontWeight.BOLD, 7*scale));
        levelText.setFill(Color.ORANGE);
        VBox ammoVbox = new VBox();
        ammoVbox.getChildren().add(ammoText);
        ammoVbox.setPadding(new Insets(10 * scale, 30*scale, 30 * scale, 150 * scale));
        ammoVbox.setAlignment(Pos.TOP_RIGHT);
        VBox levelVbox = new VBox();
        levelVbox.getChildren().add(levelText);
        levelVbox.setPadding(new Insets(10 * scale, 50 * scale, 30 * scale, 50 * scale));
        levelVbox.setAlignment(Pos.TOP_CENTER);
        String[] duckImagesFlyingBlack = {"assets/duck_black/4.png", "assets/duck_black/5.png",
                "assets/duck_black/6.png"};
        Image blackDuck = new Image("assets/duck_black/4.png");
        double blackDuckWidth = blackDuck.getWidth()*scale;
        double blackDuckHeight = blackDuck.getHeight()*scale;
        Image[] blackDuckImages = new Image[duckImagesFlyingBlack.length];
        for (int i = 0; i < duckImagesFlyingBlack.length; i++) {
            blackDuckImages[i] = new Image(Objects.requireNonNull
                    (gameSceneClass.class.getResourceAsStream(duckImagesFlyingBlack[i])));
        }
        blackDuckImageView.setFitWidth(blackDuckWidth);
        blackDuckImageView.setFitHeight(blackDuckHeight);
        blackDuckImageView.setTranslateX(-blackDuckWidth*scale);
        blackDuckImageView.setTranslateY(-67 * scale);
        blackDuckImageView.setScaleX(1);
        blackDuckImageView.setScaleY(1);
        String[] duckImagesFlyingBlue = {"assets/duck_blue/4.png", "assets/duck_blue/5.png", "assets/duck_blue/6.png"};
        Image blueDuck = new Image("assets/duck_blue/4.png");
        double blueDuckWidth = blueDuck.getWidth()*scale;
        double blueDuckHeight = blueDuck.getHeight()*scale;
        Image[] blueDuckImages = new Image[duckImagesFlyingBlue.length];
        for (int i = 0; i < duckImagesFlyingBlue.length; i++) {
            blueDuckImages[i] = new Image(Objects.requireNonNull
                    (gameSceneClass.class.getResourceAsStream(duckImagesFlyingBlue[i])));
        }
        blueDuckImageView.setFitWidth(blueDuckWidth);
        blueDuckImageView.setFitHeight(blueDuckHeight);
        blueDuckImageView.setTranslateX(blueDuckWidth*scale);
        blueDuckImageView.setTranslateY(-17*scale);
        blueDuckImageView.setScaleX(1);
        blueDuckImageView.setScaleY(1);
        final int[] currentImageIndex = {0};
        blue_flyingTimeline = new Timeline(new KeyFrame(Duration.millis(200), event -> {
            blueDuckImageView.setImage(blueDuckImages[currentImageIndex[0]]);
            currentImageIndex[0] = (currentImageIndex[0] + 1) % duckImagesFlyingBlue.length;
            double blue_initialX = blueDuckWidth*scale;
            double blue_finalX = -blueDuckWidth*scale;
            double unitMove = 8*scale;
            final double blue_currentX = blueDuckImageView.getTranslateX();
            double blue_newX = blue_currentX - unitMove;
            blueDuckImageView.setScaleX(-1);
            if (blue_newX >= blue_finalX && !blue_duckIsFlipped_x) {
                blueDuckImageView.setTranslateX(blue_newX);
            }else{
                blue_duckIsFlipped_x =true;
                blue_newX = blue_currentX + unitMove;
                blueDuckImageView.setScaleX(1);
                blueDuckImageView.setTranslateX(blue_newX);
                if (blue_currentX>blue_initialX){
                    blueDuckImageView.setScaleX(-1);
                    blue_duckIsFlipped_x =false;
                    blueDuckImageView.setTranslateX(blue_initialX);
                }
            }
        })
        );
        black_flyingTimeline = new Timeline(new KeyFrame(Duration.millis(200), event -> {
            blackDuckImageView.setImage(blackDuckImages[currentImageIndex[0]]);
            currentImageIndex[0] = (currentImageIndex[0] + 1) % duckImagesFlyingBlack.length;
            double black_initialX = -blackDuckWidth*scale;
            double black_finalX = blackDuckWidth*scale;
            double unitMove = 8*scale;
            final double black_currentX = blackDuckImageView.getTranslateX();
            double black_newX = black_currentX + unitMove;
            if (black_newX <= black_finalX && !black_duckIsFlipped_x) {
                blackDuckImageView.setTranslateX(black_newX);
            }else{
                black_duckIsFlipped_x =true;
                black_newX = black_currentX - unitMove;
                blackDuckImageView.setScaleX(-1);
                blackDuckImageView.setTranslateX(black_newX);
                if (black_currentX<black_initialX){
                    blackDuckImageView.setScaleX(1);
                    black_duckIsFlipped_x =false;
                    blackDuckImageView.setTranslateX(black_initialX);
                }
            }
        })
        );
        black_flyingTimeline.setCycleCount(Animation.INDEFINITE);
        blue_flyingTimeline.setCycleCount(Animation.INDEFINITE);
        black_flyingTimeline.stop();
        blue_flyingTimeline.stop();
        black_flyingTimeline.play();
        blue_flyingTimeline.play();
        if(black_fallingTimeline!=null){
            black_fallingTimeline.stop();
        }if(blue_fallingTimeline!=null){
            blue_fallingTimeline.stop();
        }
        levelThree.getChildren().clear();
        levelThree.getChildren().addAll(bulletMediaView, ammoVbox, levelVbox, blackDuckImageView, blueDuckImageView,
                menuClass.foreground);
        gameWon = false;
        gameOver = false;
        black_duckIsFlipped_x=false;
        blue_duckIsFlipped_x=false;
        return levelThree;
    }
    /**
     * Enables the keyboard/mouse interactions.
     * Left mouse button does the "shooting" action, crosshair takes place of the default cursor.
     * If a duck is shot, falling animation and other actions take place.
     * If level is won, pressing enter sets the fourth level scene and starts the fourth level.
     * If level is lost, pressing enter sets the third level scene and restarts the third level; pressing escape key
     * returns the player to the title scene.
     *
     * @param    primaryStage       to set a scene to the stage with related keyboard interactions
     * @param    levelThreeScene    for keyboard/mouse interactions and the selected crosshair replacing the default cursor
     * @param    scale              for scaling every node of the scenes
     * @param    levelThree         for adding and removing nodes in related cases
     */
    public void start(Stage primaryStage, Scene levelThreeScene, double scale, StackPane levelThree, double volume){
        VBox gOverVbox1 = new VBox(), gOverVbox2 = new VBox(), youWinVbox1 = new VBox(), youWinVbox2 = new VBox();
        levelThreeScene.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                if (!gameOver && !gameWon && duck!=0) {
                    bulletMedia.stop();
                    bulletMedia.setVolume(volume);
                    bulletMedia.play();
                    ammo--;
                    ammoText.setText("Ammo Left: " + ammo);
                    double bulletX = event.getX();
                    double bulletY = event.getY();
                    Bounds boundsInParentBlackDuck = blackDuckImageView.getBoundsInParent();
                    double black_xMin = boundsInParentBlackDuck.getMinX();
                    double black_xMax = boundsInParentBlackDuck.getMaxX();
                    double black_yMax = boundsInParentBlackDuck.getMaxY();
                    double black_yMin = boundsInParentBlackDuck.getMinY();
                    if (bulletX <= black_xMax && black_xMin <= bulletX && bulletY <= black_yMax && black_yMin <= bulletY){
                        duck--;
                        black_flyingTimeline.stop();
                        ImageView shotDuckBlack = new ImageView("assets/duck_black/7.png");
                        ImageView fallingDuckBlack = new ImageView("assets/duck_black/8.png");
                        if (black_duckIsFlipped_x){
                            shotDuckBlack.setScaleX(-1);
                            fallingDuckBlack.setScaleX(-1);
                        }
                        blackDuckImageView.setImage(shotDuckBlack.getImage());
                        Timeline black_shotImageChange = new Timeline(new KeyFrame(Duration.seconds(0.5), event2 -> {
                            blackDuckImageView.setImage(fallingDuckBlack.getImage());
                        }));
                        black_shotImageChange.play();
                        black_shotImageChange.setOnFinished(event1 -> {
                            fallingMedia.stop();
                            fallingMedia.setVolume(volume);
                            fallingMedia.play();
                            black_fallingTimeline = new Timeline(new KeyFrame(Duration.seconds(0.2), event2 -> {
                                double unitMove = 8*scale;
                                final double black_currentY = blackDuckImageView.getTranslateY();
                                double black_newY = black_currentY + unitMove;
                                blackDuckImageView.setTranslateY(black_newY);
                            }));
                            black_fallingTimeline.setCycleCount(Animation.INDEFINITE);
                            black_fallingTimeline.play();
                            black_fallingTimeline.setOnFinished(event2 -> {
                                fallingMedia.stop();
                            });
                        });
                    }
                    Bounds boundsInParentBlueDuck = blueDuckImageView.getBoundsInParent();
                    double blue_xMin = boundsInParentBlueDuck.getMinX();
                    double blue_xMax = boundsInParentBlueDuck.getMaxX();
                    double blue_yMax = boundsInParentBlueDuck.getMaxY();
                    double blue_yMin = boundsInParentBlueDuck.getMinY();
                    if (bulletX <= blue_xMax && blue_xMin <= bulletX && bulletY <= blue_yMax && blue_yMin <= bulletY) {
                        duck--;
                        blue_flyingTimeline.stop();
                        ImageView shotDuckBlue = new ImageView("assets/duck_blue/7.png");
                        ImageView fallingDuckBlue = new ImageView("assets/duck_blue/8.png");
                        if (blue_duckIsFlipped_x) {
                            shotDuckBlue.setScaleX(-1);
                            fallingDuckBlue.setScaleX(-1);
                        }
                        blueDuckImageView.setImage(shotDuckBlue.getImage());
                        Timeline blue_shotImageChange = new Timeline(new KeyFrame(Duration.seconds(0.5), event2 -> {
                            blueDuckImageView.setImage(fallingDuckBlue.getImage());
                        }));
                        blue_shotImageChange.play();
                        blue_shotImageChange.setOnFinished(event1 -> {
                            fallingMedia.stop();
                            fallingMedia.setVolume(volume);
                            fallingMedia.play();
                            blue_fallingTimeline = new Timeline(new KeyFrame(Duration.seconds(0.2), event2 -> {
                                double unitMove = 8*scale;
                                final double currentY = blueDuckImageView.getTranslateY();
                                double newY = currentY + unitMove;
                                blueDuckImageView.setTranslateY(newY);
                            }));
                            blue_fallingTimeline.setCycleCount(Animation.INDEFINITE);
                            blue_fallingTimeline.play();
                            blue_fallingTimeline.setOnFinished(event2 -> {
                                fallingMedia.stop();
                            });
                        });
                    }if (duck==0){
                        gameWon = true;
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
                        levelThree.getChildren().addAll(youWinVbox1, youWinVbox2);
                        levelWonMedia.setVolume(volume);
                        levelWonMedia.play();
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
                    levelThree.getChildren().removeAll(gOverVbox1, gOverVbox2);
                    levelThree.getChildren().addAll(gOverVbox1, gOverVbox2);
                    gOverMedia.setVolume(volume);
                    gOverMedia.play();
                }
            }
        });
        levelThreeScene.setOnMouseEntered(event -> {
            levelThree.getChildren().add(menuClass.selectedCursor);
        });
        levelThreeScene.setOnMouseMoved(event -> {
            menuClass.selectedCursor.setTranslateX(event.getX()-levelThreeScene.getWidth()/2);
            menuClass.selectedCursor.setTranslateY(event.getY()-levelThreeScene.getHeight()/2);
        });
        levelThreeScene.setOnMouseExited(event -> {
            levelThree.getChildren().remove(menuClass.selectedCursor);
        });
        levelThreeScene.setCursor(Cursor.NONE);
        levelThreeScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (Objects.requireNonNull(event.getCode()) == KeyCode.ENTER) {
                    levelWonMedia.stop();
                    if (gameOver) {
                        gameOver = false;
                        duck = 2;
                        ammo = duck * 3;
                        blue_fallingTimeline.stop();
                        black_fallingTimeline.stop();
                        blue_flyingTimeline.stop();
                        black_flyingTimeline.stop();
                        ammoText.setText("Ammo Left: " + ammo);
                        StackPane tempStackPane = levelThreeSetter(scale);
                        Scene tempScene = new Scene
                                (tempStackPane, DuckHunt.width * scale, DuckHunt.height * scale);
                        primaryStage.setScene(tempScene);
                        levelThreeClass levThreeScene = new levelThreeClass();
                        levThreeScene.start(primaryStage, tempScene, scale, tempStackPane, volume);
                    } else if (gameWon) {
                        black_fallingTimeline.stop();
                        blue_fallingTimeline.stop();
                        StackPane root4 = levelFourClass.levelFourSetter(scale);
                        Scene levelFourScene = new Scene
                                (root4, DuckHunt.width * scale, DuckHunt.height * scale);
                        primaryStage.setScene(levelFourScene);
                        levelFourClass gameScene4 = new levelFourClass();
                        gameScene4.start(primaryStage, levelFourScene, scale, root4, volume);
                    }
                }if (Objects.requireNonNull(event.getCode()) == KeyCode.ESCAPE) {
                    gOverMedia.stop();
                    if (gameOver) {
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

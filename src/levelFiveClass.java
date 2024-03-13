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
 * Has the necessary methods to set the fifth level scene and its keyboard/mouse interactions.
 *
 */
public class levelFiveClass {
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
    private static ImageView blackDuckImageView = new ImageView(), blueDuckImageView = new ImageView(),
            redDuckImageView = new ImageView();
    private static Timeline black_flyingTimeline = new Timeline(), blue_flyingTimeline = new Timeline(),
            red_flyingTimeline = new Timeline();
    private static Timeline black_fallingTimeline = new Timeline(), blue_fallingTimeline = new Timeline(),
            red_fallingTimeline = new Timeline();
    private static boolean gameOver, gameWon, black_duckIsFlipped_x, blue_duckIsFlipped_x, red_duckIsFlipped_x,
            red_duckIsFlipped_y = false;
    private static int duck=3, ammo = duck*3, level = 5;
    /**
     * Creates the nodes of the root: background, texts, ducks and their flying/reflecting animations, mediaview and
     * the foreground.
     *
     * @return   StackPane  to create the scene
     * @param    scale      for scaling every node of the scene
     */
    public static StackPane levelFiveSetter(double scale){
        duck = 3;
        ammo = duck*3;
        String menuBackgroundSize = String.format("%fpx %fpx", scale * DuckHunt.width, scale * DuckHunt.height);
        StackPane levelFive = new StackPane();
        levelFive.setStyle("-fx-background-image: url('" + menuClass.selectedBackgroundPath + "');"
                + "-fx-background-size: " + menuBackgroundSize + ";" + "-fx-background-repeat: no-repeat;");
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
        String[] duckImagesFlyingBlack = {"assets/duck_black/4.png", "assets/duck_black/5.png",
                "assets/duck_black/6.png"};
        Image blackDuck = new Image("assets/duck_black/4.png");
        double width = blackDuck.getWidth()*scale;
        double height = blackDuck.getHeight()*scale;
        Image[] blackDuckImages = new Image[duckImagesFlyingBlack.length];
        for (int i = 0; i < duckImagesFlyingBlack.length; i++) {
            blackDuckImages[i] = new Image(Objects.requireNonNull
                    (gameSceneClass.class.getResourceAsStream(duckImagesFlyingBlack[i])));
        }
        blackDuckImageView.setFitWidth(width);
        blackDuckImageView.setFitHeight(height);
        blackDuckImageView.setTranslateX(-width*scale);
        blackDuckImageView.setTranslateY(-67 * scale);
        blackDuckImageView.setScaleX(1);
        blackDuckImageView.setScaleY(1);
        String[] redDuckImagesFlying = {"assets/duck_red/1.png", "assets/duck_red/2.png", "assets/duck_red/3.png"};
        Image[] redDuckImages = new Image[redDuckImagesFlying.length];
        for (int i = 0; i < redDuckImagesFlying.length; i++) {
            redDuckImages[i] = new Image(Objects.requireNonNull
                    (gameSceneClass.class.getResourceAsStream(redDuckImagesFlying[i])));
        }
        redDuckImageView.setFitWidth(width);
        redDuckImageView.setFitHeight(height);
        redDuckImageView.setTranslateX(-width * scale);
        redDuckImageView.setTranslateY(0*scale);
        redDuckImageView.setScaleX(1);
        redDuckImageView.setScaleY(1);
        String[] duckImagesFlyingBlue = {"assets/duck_blue/4.png", "assets/duck_blue/5.png", "assets/duck_blue/6.png"};
        Image[] blueDuckImages = new Image[duckImagesFlyingBlue.length];
        for (int i = 0; i < duckImagesFlyingBlue.length; i++) {
            blueDuckImages[i] = new Image(Objects.requireNonNull
                    (gameSceneClass.class.getResourceAsStream(duckImagesFlyingBlue[i])));
        }
        blueDuckImageView.setFitWidth(width);
        blueDuckImageView.setFitHeight(height);
        blueDuckImageView.setTranslateX(width*scale);
        blueDuckImageView.setTranslateY(-17*scale);
        blueDuckImageView.setScaleX(1);
        blueDuckImageView.setScaleY(1);
        final int[] red_currentImageIndex = {0};
        final int[] blue_currentImageIndex = {0};
        final int[] black_currentImageIndex = {0};
        red_flyingTimeline = new Timeline(new KeyFrame(Duration.millis(200), event -> {
            redDuckImageView.setImage(redDuckImages[red_currentImageIndex[0]]);
            red_currentImageIndex[0] = (red_currentImageIndex[0] + 1) % redDuckImagesFlying.length;
            double red_initialX = -width * scale;
            double red_finalX = width * scale;
            double red_initialY = -height * scale;
            double red_finalY = height * scale;
            double unitMove = 8*scale;
            final double red_currentX = redDuckImageView.getTranslateX();
            final double red_currentY = redDuckImageView.getTranslateY();
            double red_newX = red_currentX + unitMove;
            double red_newY = red_currentY + unitMove;
            redDuckImageView.setScaleY(-1);
            if (red_newY <= red_finalY && !red_duckIsFlipped_y) {
                redDuckImageView.setTranslateY(red_newY);
            } else {
                red_duckIsFlipped_y = true;
                red_newY = red_currentY - unitMove;
                redDuckImageView.setScaleY(1);
                redDuckImageView.setTranslateY(red_newY);
                if (red_currentY < red_initialY) {
                    redDuckImageView.setScaleY(-1);
                    red_duckIsFlipped_y = false;
                    redDuckImageView.setTranslateY(red_initialY);
                }
            }
            if (red_newX <= red_finalX && !red_duckIsFlipped_x) {
                redDuckImageView.setTranslateX(red_newX);
            } else {
                red_duckIsFlipped_x = true;
                red_newX = red_currentX - unitMove;
                redDuckImageView.setScaleX(-1);
                redDuckImageView.setTranslateX(red_newX);
                if (red_currentX < red_initialX) {
                    redDuckImageView.setScaleX(1);
                    red_duckIsFlipped_x = false;
                    redDuckImageView.setTranslateX(red_initialX);
                }
            }
        })
        );
        blue_flyingTimeline = new Timeline(new KeyFrame(Duration.millis(200), event -> {
            blueDuckImageView.setImage(blueDuckImages[blue_currentImageIndex[0]]);
            blue_currentImageIndex[0] = (blue_currentImageIndex[0] + 1) % duckImagesFlyingBlue.length;
            double blue_initialX = width*scale;
            double blue_finalX = -width*scale;
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
            blackDuckImageView.setImage(blackDuckImages[black_currentImageIndex[0]]);
            black_currentImageIndex[0] = (black_currentImageIndex[0] + 1) % duckImagesFlyingBlack.length;
            double black_initialX = -width*scale;
            double black_finalX = width*scale;
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
        red_flyingTimeline.setCycleCount(Animation.INDEFINITE);
        black_flyingTimeline.play();
        blue_flyingTimeline.play();
        red_flyingTimeline.play();
        if(black_fallingTimeline!=null){
            black_fallingTimeline.stop();
        }if(blue_fallingTimeline!=null){
            blue_fallingTimeline.stop();
        }if(red_fallingTimeline!=null){
            red_fallingTimeline.stop();
        }
        levelFive.getChildren().clear();
        levelFive.getChildren().addAll(bulletMediaView, ammoVbox, levelVbox, blackDuckImageView, blueDuckImageView,
                redDuckImageView, menuClass.foreground);
        gameWon = false;
        gameOver = false;
        red_duckIsFlipped_y=false;
        black_duckIsFlipped_x=false;
        blue_duckIsFlipped_x=false;
        red_duckIsFlipped_x=false;
        return levelFive;
    }
    /**
     * Enables the keyboard/mouse interactions.
     * Left mouse button does the "shooting" action, crosshair takes place of the default cursor.
     * If a duck is shot, falling animation and other actions take place.
     * If level is won, pressing enter sets the sixth level scene and starts the sixth level.
     * If level is lost, pressing enter sets the fifth level scene and restarts the fifth level; pressing escape key
     * returns the player to the title scene.
     *
     * @param    primaryStage   to set a scene to the stage with related keyboard interactions
     * @param    levelFiveScene for keyboard/mouse interactions and the selected crosshair replacing the default cursor
     * @param    scale          for scaling every node of the scenes
     * @param    levelFive      for adding and removing nodes in related cases
     */
    public void start(Stage primaryStage, Scene levelFiveScene, double scale, StackPane levelFive, double volume){
        VBox gOverVbox1 = new VBox(), gOverVbox2 = new VBox(), youWinVbox1 = new VBox(), youWinVbox2 = new VBox();
        levelFiveScene.setOnMouseClicked(event -> {
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
                        blackDuckImageView.setScaleY(1);
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
                        blueDuckImageView.setScaleY(1);
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
                    }
                    Bounds boundsInParentRedDuck = redDuckImageView.getBoundsInParent();
                    double red_xMin = boundsInParentRedDuck.getMinX();
                    double red_xMax = boundsInParentRedDuck.getMaxX();
                    double red_yMax = boundsInParentRedDuck.getMaxY();
                    double red_yMin = boundsInParentRedDuck.getMinY();
                    if (bulletX <= red_xMax && red_xMin <= bulletX && bulletY <= red_yMax && red_yMin <= bulletY) {
                        duck--;
                        red_flyingTimeline.stop();
                        ImageView shotDuckRed = new ImageView("assets/duck_red/7.png");
                        ImageView fallingDuckRed = new ImageView("assets/duck_red/8.png");
                        if (red_duckIsFlipped_x) {
                            shotDuckRed.setScaleX(-1);
                            fallingDuckRed.setScaleX(-1);
                        }
                        redDuckImageView.setScaleY(1);
                        redDuckImageView.setImage(shotDuckRed.getImage());
                        Timeline red_shotImageChange = new Timeline(new KeyFrame(Duration.seconds(0.5), event2 -> {
                            redDuckImageView.setImage(fallingDuckRed.getImage());
                        }));
                        red_shotImageChange.play();
                        red_shotImageChange.setOnFinished(event1 -> {
                            fallingMedia.stop();
                            fallingMedia.setVolume(volume);
                            fallingMedia.play();
                            red_fallingTimeline = new Timeline(new KeyFrame(Duration.seconds(0.2), event2 -> {
                                double unitMove = 8*scale;
                                final double currentY = redDuckImageView.getTranslateY();
                                double newY = currentY + unitMove;
                                redDuckImageView.setTranslateY(newY);
                            }));
                            red_fallingTimeline.setCycleCount(Animation.INDEFINITE);
                            red_fallingTimeline.play();
                            red_fallingTimeline.setOnFinished(event2 -> {
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
                        levelFive.getChildren().removeAll(youWinVbox1, youWinVbox2);
                        levelFive.getChildren().addAll(youWinVbox1, youWinVbox2);
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
                    levelFive.getChildren().removeAll(gOverVbox1, gOverVbox2);
                    levelFive.getChildren().addAll(gOverVbox1, gOverVbox2);
                    gOverMedia.setVolume(volume);
                    gOverMedia.play();
                }
            }
        });
        levelFiveScene.setOnMouseEntered(event -> {
            levelFive.getChildren().add(menuClass.selectedCursor);
        });
        levelFiveScene.setOnMouseMoved(event -> {
            menuClass.selectedCursor.setTranslateX(event.getX()-levelFiveScene.getWidth()/2);
            menuClass.selectedCursor.setTranslateY(event.getY()-levelFiveScene.getHeight()/2);
        });
        levelFiveScene.setOnMouseExited(event -> {
            levelFive.getChildren().remove(menuClass.selectedCursor);
        });
        levelFiveScene.setCursor(Cursor.NONE);
        levelFiveScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (Objects.requireNonNull(event.getCode()) == KeyCode.ENTER) {
                    levelWonMedia.stop();
                    if (gameOver) {
                        gameOver = false;
                        duck = 3;
                        ammo = duck * 3;
                        blue_fallingTimeline.stop();
                        black_fallingTimeline.stop();
                        red_fallingTimeline.stop();
                        blue_flyingTimeline.stop();
                        black_flyingTimeline.stop();
                        red_flyingTimeline.stop();
                        ammoText.setText("Ammo Left: " + ammo);
                        StackPane tempStackPane = levelFiveSetter(scale);
                        Scene tempScene = new Scene
                                (tempStackPane, DuckHunt.width * scale, DuckHunt.height * scale);
                        primaryStage.setScene(tempScene);
                        levelFiveClass levFiveScene = new levelFiveClass();
                        levFiveScene.start(primaryStage, tempScene, scale, tempStackPane, volume);
                    } else if (gameWon) {
                        black_fallingTimeline.stop();
                        blue_fallingTimeline.stop();
                        red_fallingTimeline.stop();
                        StackPane root6 = levelSixClass.levelSixSetter(scale);
                        Scene levelSixScene = new Scene
                                (root6, DuckHunt.width * scale, DuckHunt.height * scale);
                        primaryStage.setScene(levelSixScene);
                        levelSixClass gameScene6 = new levelSixClass();
                        gameScene6.start(primaryStage, levelSixScene, scale, root6, volume);
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

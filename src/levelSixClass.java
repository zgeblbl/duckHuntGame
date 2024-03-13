import javafx.animation.*;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
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
 * Has the necessary methods to set the sixth level scene and its keyboard/mouse interactions.
 *
 */
public class levelSixClass {
    URL bulletResource = getClass().getResource("assets/effects/Gunshot.mp3");
    URL gOverResource = getClass().getResource("assets/effects/GameOver.mp3");
    URL fallingResource = getClass().getResource("assets/effects/DuckFalls.mp3");
    URL successResource = getClass().getResource("assets/effects/GameCompleted.mp3");
    private static MediaPlayer bulletMedia, gOverMedia, fallingMedia, successMedia;
    {
        assert bulletResource != null;
        bulletMedia = new MediaPlayer(new Media(bulletResource.toString()));
        assert gOverResource != null;
        gOverMedia = new MediaPlayer(new Media(gOverResource.toString()));
        assert fallingResource != null;
        fallingMedia = new MediaPlayer(new Media(fallingResource.toString()));
        assert successResource != null;
        successMedia = new MediaPlayer(new Media(successResource.toString()));
    }
    static MediaView bulletMediaView = new MediaView(bulletMedia);
    private static Text ammoText, levelText, gOverText1, gOverText2;
    private static ImageView blackDuckImageView = new ImageView(), blueDuckImageView = new ImageView(),
            redDuckImageView = new ImageView();
    private static Timeline black_flyingTimeline = new Timeline(), blue_flyingTimeline = new Timeline(),
            red_flyingTimeline = new Timeline();
    private static Timeline black_fallingTimeline = new Timeline(), blue_fallingTimeline = new Timeline(),
            red_fallingTimeline = new Timeline();
    private static boolean gameOver, gameWon, black_duckIsFlipped_x, blue_duckIsFlipped_x, red_duckIsFlipped_x,
            red_duckIsFlipped_y, black_duckIsFlipped_y, blue_duckIsFlipped_y = false;
    private static int duck = 3, ammo = duck * 3, level = 6;
    /**
     * Creates the nodes of the root: background, texts, ducks and their flying/reflecting animations, mediaview and
     * the foreground.
     *
     * @return   StackPane  to create the scene
     * @param    scale      for scaling every node of the scene
     */
    public static StackPane levelSixSetter(double scale){
        duck = 3;
        ammo = duck*3;
        String menuBackgroundSize = String.format("%fpx %fpx", scale * DuckHunt.width, scale * DuckHunt.height);
        StackPane levelSix = new StackPane();
        levelSix.setStyle("-fx-background-image: url('" + menuClass.selectedBackgroundPath + "');"
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
        String[] redDuckImagesFlying = {"assets/duck_red/1.png", "assets/duck_red/2.png", "assets/duck_red/3.png"};
        Image redDuck = new Image("assets/duck_red/1.png");
        double width = redDuck.getWidth()*scale;
        double height = redDuck.getHeight()*scale;
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
        String[] blackDuckImagesFlying = {"assets/duck_black/1.png", "assets/duck_black/2.png",
                "assets/duck_black/3.png"};
        Image[] blackDuckImages = new Image[blackDuckImagesFlying.length];
        for (int i = 0; i < blackDuckImagesFlying.length; i++) {
            blackDuckImages[i] = new Image(Objects.requireNonNull
                    (gameSceneClass.class.getResourceAsStream(blackDuckImagesFlying[i])));
        }
        blackDuckImageView.setFitWidth(width);
        blackDuckImageView.setFitHeight(height);
        blackDuckImageView.setTranslateX(-width * scale);
        blackDuckImageView.setTranslateY(-67*scale);
        blackDuckImageView.setScaleX(1);
        blackDuckImageView.setScaleY(1);
        String[] blueDuckImagesFlying = {"assets/duck_blue/1.png", "assets/duck_blue/2.png", "assets/duck_blue/3.png"};
        Image[] blueDuckImages = new Image[blueDuckImagesFlying.length];
        for (int i = 0; i < blueDuckImagesFlying.length; i++) {
            blueDuckImages[i] = new Image(Objects.requireNonNull
                    (gameSceneClass.class.getResourceAsStream(blueDuckImagesFlying[i])));
        }
        blueDuckImageView.setFitWidth(width);
        blueDuckImageView.setFitHeight(height);
        blueDuckImageView.setTranslateX(width * scale);
        blueDuckImageView.setTranslateY(-10*scale);
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
            double red_currentX = redDuckImageView.getTranslateX();
            double red_currentY = redDuckImageView.getTranslateY();
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
        black_flyingTimeline = new Timeline(new KeyFrame(Duration.millis(200), event -> {
            blackDuckImageView.setImage(blackDuckImages[black_currentImageIndex[0]]);
            black_currentImageIndex[0] = (black_currentImageIndex[0] + 1) % blackDuckImagesFlying.length;
            double black_initialX = -width * scale;
            double black_finalX = width * scale;
            double black_initialY = height * scale;
            double black_finalY = -height * scale;
            double unitMove = 8*scale;
            double black_currentX = blackDuckImageView.getTranslateX();
            double black_currentY = blackDuckImageView.getTranslateY();
            double black_newX = black_currentX + unitMove;
            double black_newY = black_currentY - unitMove;
            if (black_newY >= black_finalY && !black_duckIsFlipped_y) {
                blackDuckImageView.setTranslateY(black_newY);
            } else {
                black_duckIsFlipped_y = true;
                black_newY = black_currentY + unitMove;
                blackDuckImageView.setScaleY(-1);
                blackDuckImageView.setTranslateY(black_newY);
                if (black_currentY > black_initialY) {
                    blackDuckImageView.setScaleY(1);
                    black_duckIsFlipped_y = false;
                    blackDuckImageView.setTranslateY(black_initialY);
                }
            }
            if (black_newX <= black_finalX && !black_duckIsFlipped_x) {
                blackDuckImageView.setTranslateX(black_newX);
            } else {
                black_duckIsFlipped_x = true;
                black_newX = black_currentX - unitMove;
                blackDuckImageView.setScaleX(-1);
                blackDuckImageView.setTranslateX(black_newX);
                if (black_currentX < black_initialX) {
                    blackDuckImageView.setScaleX(1);
                    black_duckIsFlipped_x = false;
                    blackDuckImageView.setTranslateX(black_initialX);
                }
            }
        })
        );
        blue_flyingTimeline = new Timeline(new KeyFrame(Duration.millis(200), event -> {
            blueDuckImageView.setImage(blueDuckImages[blue_currentImageIndex[0]]);
            blue_currentImageIndex[0] = (blue_currentImageIndex[0] + 1) % blueDuckImagesFlying.length;
            double blue_initialX = width * scale;
            double blue_finalX = -width * scale;
            double blue_initialY = height * scale;
            double blue_finalY = -height * scale;
            double unitMove = 8*scale;
            double blue_currentX = blueDuckImageView.getTranslateX();
            double blue_currentY = blueDuckImageView.getTranslateY();
            double blue_newX = blue_currentX - unitMove;
            double blue_newY = blue_currentY - unitMove;
            blueDuckImageView.setScaleX(-1);
            if (blue_newY >= blue_finalY && !blue_duckIsFlipped_y) {
                blueDuckImageView.setTranslateY(blue_newY);
            } else {
                blue_duckIsFlipped_y = true;
                blue_newY = blue_currentY + unitMove;
                blueDuckImageView.setScaleY(-1);
                blueDuckImageView.setTranslateY(blue_newY);
                if (blue_currentY > blue_initialY) {
                    blueDuckImageView.setScaleY(1);
                    blue_duckIsFlipped_y = false;
                    blueDuckImageView.setTranslateY(blue_initialY);
                }
            }
            if (blue_newX >= blue_finalX && !blue_duckIsFlipped_x) {
                blueDuckImageView.setTranslateX(blue_newX);
            } else {
                blue_duckIsFlipped_x = true;
                blue_newX = blue_currentX + unitMove;
                blueDuckImageView.setScaleX(1);
                blueDuckImageView.setTranslateX(blue_newX);
                if (blue_currentX > blue_initialX) {
                    blueDuckImageView.setScaleX(-1);
                    blue_duckIsFlipped_x = false;
                    blueDuckImageView.setTranslateX(blue_initialX);
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
        levelSix.getChildren().clear();
        levelSix.getChildren().addAll(bulletMediaView, ammoVbox, levelVbox, blackDuckImageView, blueDuckImageView,
                redDuckImageView, menuClass.foreground);
        gameWon = false;
        gameOver = false;
        black_duckIsFlipped_y=false;
        blue_duckIsFlipped_y=false;
        red_duckIsFlipped_y=false;
        black_duckIsFlipped_x=false;
        blue_duckIsFlipped_x=false;
        red_duckIsFlipped_x=false;
        return levelSix;
    }
    /**
     * Enables the keyboard/mouse interactions.
     * Left mouse button does the "shooting" action, crosshair takes place of the default cursor.
     * If a duck is shot, falling animation and other actions take place.
     * If level is won, pressing enter sets the first level scene and starts the first level; therefore, the game restarts.
     * If level is lost, pressing enter sets the sixth level scene and restarts the sixth level.
     * If the level has ended, pressing escape key returns the player to the title scene.
     *
     * @param    primaryStage   to set a scene to the stage with related keyboard interactions
     * @param    levelSixScene  for keyboard/mouse interactions and the selected crosshair replacing the default cursor
     * @param    scale          for scaling every node of the scenes
     * @param    levelSix       for adding and removing nodes in related cases
     */
    public void start(Stage primaryStage, Scene levelSixScene, double scale, StackPane levelSix, double volume){
        VBox gOverVbox1 = new VBox(), gOverVbox2 = new VBox();
        levelSixScene.setOnMouseClicked(event -> {
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
                    }
                    if (duck==0){
                        gameWon = true;
                        Text success = new Text(0, 0, "You have completed the game!\n\n");
                        success.setFont(Font.font("Arial", FontWeight.BOLD, 10*scale));
                        success.setFill(Color.ORANGE);
                        Text success2 = new Text(0, 0, "\nPress ENTER to play again\nPress ESC to exit");
                        success2.setFont(Font.font("Arial", FontWeight.BOLD, 10*scale));
                        success2.setFill(Color.ORANGE);
                        success2.setTextAlignment(TextAlignment.CENTER);
                        VBox successVbox1= new VBox();
                        successVbox1.setAlignment(Pos.CENTER);
                        VBox successVbox2= new VBox();
                        successVbox2.setAlignment(Pos.CENTER);
                        successVbox1.getChildren().addAll(success);
                        successVbox2.getChildren().addAll(success2);
                        Timeline flashingTimeline = new Timeline(
                                new KeyFrame(Duration.seconds(0.5), evt -> success2.setVisible(true)),
                                new KeyFrame(Duration.seconds(1.0), evt -> success2.setVisible(false))
                        );
                        flashingTimeline.setCycleCount(Animation.INDEFINITE);
                        flashingTimeline.play();
                        levelSix.getChildren().removeAll(successVbox1, successVbox2);
                        levelSix.getChildren().addAll(successVbox1, successVbox2);
                        successMedia.setVolume(volume);
                        successMedia.play();
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
                    levelSix.getChildren().removeAll(gOverVbox1, gOverVbox2);
                    levelSix.getChildren().addAll(gOverVbox1, gOverVbox2);
                    gOverMedia.setVolume(volume);
                    gOverMedia.play();
                }
            }
        });
        levelSixScene.setOnMouseEntered(event -> {
            levelSix.getChildren().add(menuClass.selectedCursor);
        });
        levelSixScene.setOnMouseMoved(event -> {
            menuClass.selectedCursor.setTranslateX(event.getX()-levelSixScene.getWidth()/2);
            menuClass.selectedCursor.setTranslateY(event.getY()-levelSixScene.getHeight()/2);
        });
        levelSixScene.setOnMouseExited(event -> {
            levelSix.getChildren().remove(menuClass.selectedCursor);
        });
        levelSixScene.setCursor(Cursor.NONE);
        levelSixScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case ENTER:
                        successMedia.stop();
                        if (gameOver){
                            gameOver = false;
                            duck = 3;
                            ammo = duck*3;
                            blue_fallingTimeline.stop();
                            black_fallingTimeline.stop();
                            red_fallingTimeline.stop();
                            blue_flyingTimeline.stop();
                            black_flyingTimeline.stop();
                            red_flyingTimeline.stop();
                            ammoText.setText("Ammo Left: " + ammo);
                            StackPane tempStackPane = levelSixSetter(scale);
                            Scene tempScene = new Scene
                                    (tempStackPane, DuckHunt.width*scale, DuckHunt.height*scale);
                            primaryStage.setScene(tempScene);
                            levelSixClass levSixScene = new levelSixClass();
                            levSixScene.start(primaryStage, tempScene, scale, tempStackPane, volume);
                        }else if (gameWon){
                            black_fallingTimeline.stop();
                            blue_fallingTimeline.stop();
                            red_fallingTimeline.stop();
                            StackPane root = gameSceneClass.gameSetter(scale);
                            Scene gameScene = new Scene(root, DuckHunt.width*scale, DuckHunt.height*scale);
                            primaryStage.setScene(gameScene);
                            gameSceneClass gameScene1 = new gameSceneClass();
                            gameScene1.start(primaryStage, gameScene, scale, root, volume);
                            break;
                        }
                        break;
                    case ESCAPE:
                        if (gameWon||gameOver){
                            successMedia.stop();
                            StackPane titleRoot = titleClass.titleSetter(scale, volume);
                            Scene titleScene = new Scene
                                    (titleRoot, DuckHunt.width*scale, DuckHunt.height*scale);
                            titleScene.setCursor(Cursor.DEFAULT);
                            primaryStage.setScene(titleScene);
                            titleClass titleScn = new titleClass();
                            titleScn.start(primaryStage, titleScene, scale, volume);
                        }
                        break;
                }
            }
        });
    }
}

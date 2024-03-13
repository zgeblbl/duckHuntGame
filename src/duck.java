import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;

public class duck extends Application {
    Image backgroundImage = new Image("assets/welcome/1.png");
    double width = backgroundImage.getWidth();
    double height = backgroundImage.getHeight();
    ImageView node = new ImageView(backgroundImage);
    private static double SCALE = 3.0;

    public static double getScale() {
        return SCALE;
    }
    Text beginningText, menuText, ammoText, levelText, gOverText1, gOverText2;
    ImageView cursor;
    int duck=1;
    int ammo = duck*3;
    int level = 1;
    Scene gameScene;
    boolean gameOver = false;
    StackPane game = new StackPane();
    private Group board;
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();
        double scale = 3.0;
        node.setFitHeight(height*scale);
        node.setFitWidth(width*scale);
        String backgroundSize = String.format("%fpx %fpx", scale * width, scale * height); // Calculate the scaled size
        root.setStyle("-fx-background-image: url('assets/welcome/1.png');" + "-fx-background-size: " + backgroundSize + ";" + "-fx-background-repeat: no-repeat;");
        board = new Group();
        beginningText = new Text(0, 0, "PRESS ENTER TO START\nPRESS ESC TO EXIT");
        beginningText.setFont(Font.font ("Arial", FontWeight.BOLD, 40));
        beginningText.setFill(Color.ORANGE);
        beginningText.setTextAlignment(TextAlignment.CENTER);
        VBox vbox = new VBox();
        vbox.getChildren().add(beginningText);
        vbox.setPadding(new Insets(130*scale, 30*scale, 30*scale, 30*scale));
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.5), evt -> vbox.setVisible(false)),
                new KeyFrame(Duration.seconds( 1.2), evt -> vbox.setVisible(true)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        URL resource = getClass().getResource("assets/effects/Title.mp3");
        assert resource != null;
        MediaPlayer titleMedia =new MediaPlayer(new Media(resource.toString()));
        titleMedia.setOnEndOfMedia(new Runnable() {
            public void run() {
                titleMedia.seek(Duration.ZERO);
            }
        });
        titleMedia.play();
        MediaView media = new MediaView(titleMedia);
        board.getChildren().addAll(vbox, media);
        root.getChildren().add(board);
        Scene introScene = new Scene(root);
        ArrayList<ImageView> crosshairs = new ArrayList<>();
        for (int i=1; i<8; i++) {
            String path = "assets/crosshair/" + i + ".png";
            cursor = new ImageView(path);
            cursor.setFitWidth(cursor.getImage().getWidth() * scale);
            cursor.setFitHeight(cursor.getImage().getHeight() * scale);
            cursor.setLayoutX(width*scale/2);
            cursor.setLayoutY(height*scale/2);
            crosshairs.add(cursor);
        }

        StackPane menu = new StackPane();
        URL gOverResource = getClass().getResource("assets/effects/GameOver.mp3");
        assert gOverResource != null;
        MediaPlayer gOverMedia =new MediaPlayer(new Media(gOverResource.toString()));
        URL introResource = getClass().getResource("assets/effects/Intro.mp3");
        assert introResource != null;
        MediaPlayer introMedia =new MediaPlayer(new Media(introResource.toString()));
        MediaView menuMediaView = new MediaView(introMedia);
        URL bulletResource = getClass().getResource("assets/effects/Gunshot.mp3");
        assert bulletResource != null;
        MediaPlayer bulletMedia =new MediaPlayer(new Media(bulletResource.toString()));
        MediaView bulletMediaView = new MediaView(bulletMedia);
        String menuBackgroundSize = String.format("%fpx %fpx", scale * width, scale * height);
        ArrayList<String> backgroundPaths = new ArrayList<>();
        for (int i=1; i<7; i++) {
            String path = "assets/background/" + i + ".png";
            backgroundPaths.add(path);
        }
        String backgroundPath = "assets/background/1.png";
        menu.setStyle("-fx-background-image: url('"+backgroundPath+"');" + "-fx-background-size: " + menuBackgroundSize
                + ";" + "-fx-background-repeat: no-repeat;");
        menuText = new Text(0, 0, "USE ARROW KEYS TO NAVIGATE\nPRESS ENTER TO START");
        menuText.setFont(Font.font ("Arial", FontWeight.BOLD, 20));
        menuText.setFill(Color.ORANGE);
        menuText.setTextAlignment(TextAlignment.CENTER);
        VBox menuVBox = new VBox();
        menuVBox.getChildren().add(menuText);
        menuVBox.setPadding(new Insets(20*scale, 150, 30*scale, 200));
        cursor = new ImageView("assets/crosshair/1.png");
        cursor.setFitWidth(cursor.getImage().getWidth()*scale);
        cursor.setFitHeight(cursor.getImage().getHeight()*scale);
        cursor.setLayoutX(100);
        cursor.setLayoutY(150);
        menu.getChildren().addAll(menuVBox, cursor, menuMediaView);
        Scene menuScene = new Scene(menu, 300, 200);
        final int[] currentCursor = {0};
        final int[] backgPath = {0};



        menuScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case ENTER:
                        titleMedia.stop();
                        introMedia.play();
                        introMedia.setOnEndOfMedia(() -> {
                            String gameBackPath = backgroundPaths.get(backgPath[0]);
                            game.setStyle("-fx-background-image: url('"+gameBackPath+"');" + "-fx-background-size: "
                                    + menuBackgroundSize + ";" + "-fx-background-repeat: no-repeat;");
                            gameScene = new Scene(game, 300, 200);
                            gameScene.setCursor(new ImageCursor(crosshairs.get(currentCursor[0]).getImage()));
                            ammoText = new Text(0, 0, "Ammo Left: "+ammo);
                            ammoText.setFont(Font.font ("Arial", FontWeight.BOLD, 20));
                            ammoText.setFill(Color.ORANGE);
                            levelText = new Text(0, 0, "Level "+level);
                            levelText.setFont(Font.font ("Arial", FontWeight.BOLD, 20));
                            levelText.setFill(Color.ORANGE);
                            VBox ammoVbox = new VBox();
                            ammoVbox.getChildren().add(ammoText);
                            ammoVbox.setPadding(new Insets(10*scale, 100, 30*scale, 150*scale));
                            ammoVbox.setAlignment(Pos.TOP_RIGHT);
                            VBox levelVbox = new VBox();
                            levelVbox.getChildren().add(levelText);
                            levelVbox.setPadding(new Insets(10*scale, 50*scale, 30*scale, 50*scale));
                            levelVbox.setAlignment(Pos.TOP_CENTER);
                            game.getChildren().addAll(bulletMediaView, ammoVbox, levelVbox);

                            gameScene.setOnMouseClicked(event1 -> {
                                if (event1.getButton() == MouseButton.PRIMARY) {
                                    if(!gameOver){
                                        bulletMedia.stop();
                                        bulletMedia.play();
                                        ammo--;
                                        ammoText.setText("Ammo Left: "+ammo);
                                    }
                                    if (ammo==0){
                                        gameOver = true;
                                        gOverText1 = new Text(0, 0, "GAME OVER!\n\n");
                                        gOverText1.setFont(Font.font ("Arial", FontWeight.BOLD, 30));
                                        gOverText1.setFill(Color.ORANGE);
                                        gOverText2 = new Text(0, 0, "\nPress ENTER to play again\nPress ESC to exit");
                                        gOverText2.setFont(Font.font ("Arial", FontWeight.BOLD, 30));
                                        gOverText2.setFill(Color.ORANGE);
                                        gOverText2.setTextAlignment(TextAlignment.CENTER);
                                        VBox gOverVbox1 = new VBox();
                                        gOverVbox1.setAlignment(Pos.CENTER);
                                        VBox gOverVbox2 = new VBox();
                                        gOverVbox2.setAlignment(Pos.CENTER);
                                        gOverVbox1.getChildren().addAll(gOverText1);
                                        gOverVbox2.getChildren().addAll(gOverText2);
                                        Timeline flashingTimeline = new Timeline(
                                                new KeyFrame(Duration.seconds(0.5), evt -> gOverText2.setVisible(true)),
                                                new KeyFrame(Duration.seconds(1.0), evt -> gOverText2.setVisible(false))
                                        );
                                        flashingTimeline.setCycleCount(Animation.INDEFINITE);
                                        flashingTimeline.play();
                                        game.getChildren().addAll(gOverVbox1, gOverVbox2);
                                        gOverMedia.play();
                                    }
                                }
                            });
                            primaryStage.setScene(gameScene);
                        });
                        break;
                    case UP:
                        menu.getChildren().remove(cursor);
                        if (currentCursor[0] ==6){
                            menu.getChildren().remove(crosshairs.get(6));
                            menu.getChildren().add(crosshairs.get(0));
                            currentCursor[0] = 0;
                        }else{
                            menu.getChildren().remove(crosshairs.get(currentCursor[0]));
                            menu.getChildren().add(crosshairs.get(currentCursor[0] +1));
                            currentCursor[0]++;
                        }
                        break;
                    case DOWN:
                        menu.getChildren().remove(cursor);
                        if (currentCursor[0] ==0){
                            menu.getChildren().remove(crosshairs.get(0));
                            menu.getChildren().add(crosshairs.get(6));
                            currentCursor[0] = 6;
                        }else{
                            menu.getChildren().remove(crosshairs.get(currentCursor[0]));
                            menu.getChildren().add(crosshairs.get(currentCursor[0] -1));
                            currentCursor[0]--;
                        }
                        break;
                    case RIGHT:
                        if (backgPath[0] == 5){
                            String backgroundPath = backgroundPaths.get(0);
                            menu.setStyle("-fx-background-image: url('"+backgroundPath+"');" + "-fx-background-size: "
                                    + menuBackgroundSize + ";" + "-fx-background-repeat: no-repeat;");
                            backgPath[0] = 0;

                        }else{
                            String backgroundPath = backgroundPaths.get((backgPath[0] +1));
                            menu.setStyle("-fx-background-image: url('"+backgroundPath+"');" + "-fx-background-size: "
                                    + menuBackgroundSize + ";" + "-fx-background-repeat: no-repeat;");
                            backgPath[0]++;
                        }
                        break;
                    case LEFT:
                        if (backgPath[0] == 0){
                            String backgroundPath = backgroundPaths.get(5);
                            menu.setStyle("-fx-background-image: url('"+backgroundPath+"');" + "-fx-background-size: "
                                    + menuBackgroundSize + ";" + "-fx-background-repeat: no-repeat;");
                            backgPath[0] = 5;

                        }else{
                            String backgroundPath = backgroundPaths.get((backgPath[0]-1));
                            menu.setStyle("-fx-background-image: url('"+backgroundPath+"');" + "-fx-background-size: "
                                    + menuBackgroundSize + ";" + "-fx-background-repeat: no-repeat;");
                            backgPath[0]--;
                        }
                        break;
                }
            }
        });
        introScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case ENTER:
                        primaryStage.setScene(menuScene);
                        break;
                    case ESCAPE:
                        primaryStage.close();
                        break;
                }
            }
        });
        primaryStage.setWidth(scale * width);
        primaryStage.setHeight(scale * height);
        primaryStage.setScene(introScene);
        primaryStage.show();
    }
    public static void main(String[] args){
        launch(args);
    }
}

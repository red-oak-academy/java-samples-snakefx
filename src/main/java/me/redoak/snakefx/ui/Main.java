package me.redoak.snakefx.ui;

import me.redoak.snakefx.logic.GameLogic;
import me.redoak.snakefx.logic.GameState;
import me.redoak.snakefx.logic.SnakeDirection;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

    private int gameSizeX = 40;
    private int gameSizeY = 40;

    private GameField gameField = new GameField(gameSizeX, gameSizeY);
    private GameLogic game = new GameLogic(gameSizeX, gameSizeY);

    private GameThread tickThread = null;

    private Label gameStatus = new Label("Lets Play SnakeFX");

    @Override
    public void init() {
        gameField = new GameField(gameSizeX, gameSizeY);
    }

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("SnakeFX");

        BorderPane container = new BorderPane();
        container.setTop(createTopContainer());
        container.setCenter(createCenterContainer());
        container.setBottom(createBottomContainer());
        container.setLeft(createLeftContainer());
        container.setRight(createRightContainer());

        Scene scene = new Scene(container);

        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if(event.getCode() == KeyCode.SPACE) {
                this.handleSpaceKey();
            } else if(event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.S) {
                this.game.setSnakeDirection(SnakeDirection.DOWN);
            } else if(event.getCode() == KeyCode.UP || event.getCode() == KeyCode.W) {
                this.game.setSnakeDirection(SnakeDirection.UP);
            } else if(event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.A) {
                this.game.setSnakeDirection(SnakeDirection.LEFT);
            } else if(event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.D) {
                this.game.setSnakeDirection(SnakeDirection.RIGHT);
            }
        });

        game.addGamePositionListener((snakeCoordinates, foodCoordinate) -> {
            Platform.runLater(() -> {
                this.gameField.clearFields();
                this.gameField.drawSnakeAndFood(snakeCoordinates, foodCoordinate);
            });
        });

        game.addGameStateListener(gameState -> {
            Platform.runLater(() -> {
                this.gameStatus.setText(gameState.name());
            });
            if (gameState == GameState.LOSE) {
                this.tickThread.setRunning(false);
            }
        });

        primaryStage.setScene(scene);
        primaryStage.setOnHiding((event) -> this.tickThread.setRunning(false));
        primaryStage.show();
    }

    private void handleSpaceKey() {
        gameField.clearFields();
        game.startGame();
        this.tickThread = new GameThread(game);
        this.tickThread.start();
    }

    private Pane createTopContainer() {

        Label label = new Label("SnakeFX");
        label.setFont(Font.font(25));

        HBox topContainer = new HBox();
        topContainer.setAlignment(Pos.CENTER);
        topContainer.getChildren().add(label);
        topContainer.setPadding(new Insets(10, 0, 10, 0));

        return topContainer;
    }

    private Pane createCenterContainer() {

        HBox gameFieldContainer = new HBox();
        gameFieldContainer.getChildren().add(gameField);
        gameFieldContainer.setAlignment(Pos.CENTER);

        return gameFieldContainer;
    }

    private Pane createBottomContainer() {

        HBox container = new HBox();
        container.getChildren().add(gameStatus);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(10, 10, 10, 10));

        return container;
    }

    private Pane createLeftContainer() {
        return null;
    }

    private Pane createRightContainer() {

        Label controlsLabel = new Label("Steuerung: \n \n " +
                "Space: Neues Spiel Starten");

        VBox container = new VBox();
        container.getChildren().add(controlsLabel);
        container.setAlignment(Pos.CENTER_LEFT);
        container.setPadding(new Insets(10, 10, 10, 10));

        return container;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package me.redoak.snakefx.ui;

import me.redoak.snakefx.logic.Coordinate;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.List;

public class GameField extends GridPane {

    private Rectangle[][] gameTiles;

    public GameField(int x, int y) {
        super();

        this.gameTiles = new Rectangle[x][y];

        for (int i = 0; i < x * y; i++) {
            Rectangle gameTile = new Rectangle();
            gameTile.setWidth(10);
            gameTile.setHeight(10);
            gameTile.setFill(i % 3 == 0 ? Color.RED : Color.GREEN);
            gameTiles[i % x][i / x] = gameTile;
            this.add(gameTiles[i % x][i / x], i % x, i / x);
        }
    }

    public void setField(boolean active, int x, int y) {
        this.setField(active ? Color.BLACK : Color.WHITE, x, y);
    }

    public void setField(Color color, int x, int y) {
        gameTiles[x][y].setFill(color);
    }

    public void clearFields() {
        for(int x = 0; x < gameTiles.length; x++) {
            for(int y = 0; y < gameTiles[x].length; y++) {
                gameTiles[x][y].setFill(Color.WHITE);
            }
        }
    }

    public void drawSnakeAndFood(final List<Coordinate> snakeCoordinates, final Coordinate food) {
        snakeCoordinates.forEach(coordinate -> this.setField(true, coordinate.getX(), coordinate.getY()));
        this.setField(Color.RED, food.getX(), food.getY());

    }
}

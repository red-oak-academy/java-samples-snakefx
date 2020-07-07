package me.redoak.snakefx.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static me.redoak.snakefx.logic.GameState.LOSE;
import static me.redoak.snakefx.logic.GameState.RUNNING;

public class GameLogic {

    private List<Consumer<GameState>> gameStateListener = new ArrayList<>();
    private List<BiConsumer<List<Coordinate>, Coordinate>> gamePositionListener = new ArrayList<>();

    private final int gameSizeX;
    private final int gameSizeY;

    private SnakeDirection snakeDirection;
    private List<Coordinate> snakePositions;

    private Coordinate foodPosition;
    private GameState gameState;

    public GameLogic(int x, int y) {
        super();

        this.gameSizeX = x;
        this.gameSizeY = y;
    }

    public void startGame() {

        System.out.println("Starting new Game.");

        this.snakeDirection = SnakeDirection.LEFT;;
        this.snakePositions = new ArrayList<>();
        snakePositions.add(new Coordinate(gameSizeX / 2, gameSizeY / 2));
        foodPosition = new Coordinate((gameSizeX / 2) + 2, (gameSizeY / 2) + 2);

        this.setGameState(RUNNING);

        this.notifyGamePositionListeners();
    }

    public void tick() {

        if(this.gameState == RUNNING) {

            SnakeDirection dir = snakeDirection;

            //First, find the new head position
            final Coordinate snakeHead = this.snakePositions.get(0);
            int newHeadX = snakeHead.getX();
            int newHeadY = snakeHead.getY();

            switch (dir) {
                case NONE:
                    break;
                case LEFT:
                    --newHeadX;
                    break;
                case UP:
                    --newHeadY;
                    break;
                case RIGHT:
                    ++newHeadX;
                    break;
                case DOWN:
                    ++newHeadY;
                    break;
            }

            Coordinate newHeadCoord = new Coordinate(newHeadX, newHeadY);

            //now, check if we collide with anything
            if(newHeadX < 0 || newHeadX >= gameSizeX || newHeadY < 0 || newHeadY >= gameSizeY || snakePositions.contains(newHeadCoord)) {
                System.out.println("Ouch! That hurts!\nGame failed!");
                this.setGameState(LOSE);
                return;
            }

            snakePositions.add(0, newHeadCoord);

            if(foodPosition.equals(newHeadCoord)) {
                System.out.println("Yummy! New Food being generated!");
                generateNewFoodPosition();
            } else {
                snakePositions.remove(snakePositions.size()-1);
            }
        }

        this.notifyGameStateListeners();
        this.notifyGamePositionListeners();
    }

    private void generateNewFoodPosition() {
        do {
            this.foodPosition = Coordinate.random(gameSizeX - 4,gameSizeY - 4);
        } while (snakePositions.contains(this.foodPosition));
    }

    private void notifyGamePositionListeners() {
        this.gamePositionListener.forEach((listener) -> listener.accept(this.snakePositions, this.foodPosition));
    }

    private void notifyGameStateListeners() {
        this.gameStateListener.forEach((listener) -> listener.accept(this.gameState));
    }

    public final int getCurrentTickSpeed() {
        int tickspeed = 500 / ((this.snakePositions.size()*10+10) / 10);
        System.out.println("Current tick speed: " + tickspeed);
        return tickspeed;
    }

    public void setSnakeDirection(SnakeDirection snakeDirection) {
        this.snakeDirection = snakeDirection;
    }

    public void addGamePositionListener(BiConsumer<List<Coordinate>, Coordinate> gameFieldPositionListener) {
        this.gamePositionListener.add(gameFieldPositionListener);
    }

    public void addGameStateListener(Consumer<GameState> listener) {
        this.gameStateListener.add(listener);
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public GameState getGameState() {
        return this.gameState;
    }
}

package me.redoak.snakefx.ui;

import me.redoak.snakefx.logic.GameLogic;

public class GameThread extends Thread {
    private boolean isRunning;
    private GameLogic game;

    public GameThread(GameLogic game) {
        this.isRunning = true;
        this.game = game;
    }

    @Override
    public void run() {
        while(this.isRunning) {
            this.game.tick();
            try {
                Thread.sleep(this.game.getCurrentTickSpeed());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }
}


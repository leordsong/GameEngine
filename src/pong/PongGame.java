package pong;

import core.*;

public class PongGame extends Game {

    private static final int WALL_WIDTH = 20;

    public static void main(String[] args) {
        PongGame pongGame = new PongGame();

        pongGame.addGameObject(new Wall("left", new Vector2(0, 0), new Vector2(WALL_WIDTH, WINDOW_HEIGHT)));
        pongGame.addGameObject(new Wall("right", new Vector2(WINDOW_WIDTH - WALL_WIDTH, 0), new Vector2(20, WINDOW_HEIGHT)));
        pongGame.addGameObject(new Wall("top", new Vector2(0, 0), new Vector2(WINDOW_WIDTH, WALL_WIDTH)));
        pongGame.addGameObject(new Wall("bottom", new Vector2(0, WINDOW_HEIGHT - WALL_WIDTH - WALL_WIDTH), new Vector2(WINDOW_WIDTH, WALL_WIDTH)));

        pongGame.addGameObject(new Ball());
        pongGame.addGameObject(new Paddle());

        pongGame.startGame();
    }
}
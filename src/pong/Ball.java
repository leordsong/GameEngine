package pong;

import java.awt.Color;
import java.util.Optional;

import core.*;

public class Ball extends GameObject {

    private static final float SPEED = 120f;
    private final float ROOT_2 = (float) Math.sqrt(2);
    private Vector2 velocity = new Vector2(-ROOT_2, -ROOT_2);

    public Ball() {
        super("Ball");
    }

    @Override
    public void start() {
        getTransform().setSize(new Vector2(40, 40));
        Game game = Optional.ofNullable(getGame()).orElseThrow(IllegalStateException::new);
        game.getAudioEngine().addClip("boing", "..\\Media\\blip.wav");
        getTransform().setPosition(new Vector2(Game.WINDOW_WIDTH / 2f, Game.WINDOW_HEIGHT / 2f));
        getMaterial().setColor(Color.RED);
    }

    @Override
    public void update(float elapsedTime) {
        getTransform().updatePosition(velocity.getX() * SPEED * elapsedTime, velocity.getY() * SPEED * elapsedTime);
        if (getTransform().getPosition().getX() < 0)
            getTransform().setPosition(new Vector2(0, getTransform().getPosition().getY()));
        else if (getTransform().getPosition().getX() > Game.WINDOW_WIDTH)
            getTransform().setPosition(new Vector2(Game.WINDOW_WIDTH, getTransform().getPosition().getY()));
        if (getTransform().getPosition().getY() < 0)
            getTransform().setPosition(new Vector2(getTransform().getPosition().getX(), 0));
        else if (getTransform().getPosition().getY() > Game.WINDOW_HEIGHT)
            getTransform().setPosition(new Vector2(getTransform().getPosition().getX(), Game.WINDOW_HEIGHT));
    }

    @Override
    public void onCollisionEnter(GameObject col) {
        Game game = Optional.ofNullable(getGame()).orElseThrow(IllegalStateException::new);
        game.getAudioEngine().playOneShot("boing");

        // add code to change direction
        if (col.getName().equals("top") || col.getName().equals("bottom")) {
            velocity = new Vector2(velocity.getX(), -velocity.getY());
        } else {
            assert (col.getName().equals("right") || col.getName().equals("left") || col.getName().equals("paddle"));
            velocity = new Vector2(-velocity.getX(), velocity.getY());
        }
    }
}

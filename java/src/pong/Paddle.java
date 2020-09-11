package pong;

import java.awt.Color;
import java.awt.event.KeyEvent;

import core.*;

public class Paddle extends GameObject {

    private static final float SPEED = 170f;
    private boolean atTop = false;
    private boolean atBottom = false;

    public Paddle() {
        super("paddle");
    }

    @Override
    public void start() {
        getTransform().setPosition(new Vector2(700, 300));
        getTransform().setSize(new Vector2(20, 130));
        getMaterial().setColor(Color.BLUE);
    }

    @Override
    public void update(float elapsedTime) {
        if (InputEngine.getInstance().getKey(KeyEvent.VK_DOWN) && !atBottom) {
            // move paddle down
            getTransform().updatePosition(0, elapsedTime * SPEED);
        } else if (InputEngine.getInstance().getKey(KeyEvent.VK_UP) && !atTop) {
            // move paddle up
            getTransform().updatePosition(0, -elapsedTime * SPEED);
        }
    }

    @Override
    public void onCollisionEnter(GameObject col) {
        if (col.getName().equals("bottom")) {
            atBottom = true;
        }
        if (col.getName().equals("top")) {
            atTop = true;
        }
    }

    @Override
    public void onCollisionExit(GameObject col) {
        if (col.getName().equals("bottom")) {
            atBottom = false;
        }
        if (col.getName().equals("top")) {
            atTop = false;
        }
    }
}
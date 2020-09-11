package pong;

import java.awt.Color;

import core.*;

public class Wall extends GameObject {

    public void start() {
        getMaterial().setColor(Color.orange);
    }

    public Wall(String name, Vector2 pos, Vector2 size) {
        super(name);
        getTransform().setPosition(pos);
        getTransform().setSize(size);
    }
}

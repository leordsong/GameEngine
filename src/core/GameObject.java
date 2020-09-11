package core;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class GameObject {
    private final String name;
    private final Transform transform;
    private final Material material;
    private Game game;

    public GameObject(String name) {
        this.name = Optional.ofNullable(name).orElseThrow(IllegalArgumentException::new);
        // Create default components
        transform = new Transform();
        material = new Material();
    }

    public void start() {

    }

    public void update(float elapsedTime) {

    }

    public void onCollisionEnter(GameObject col) {

    }

    public void onCollisionExit(GameObject col) {

    }

    @Nullable
    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = Optional.ofNullable(game).orElseThrow(IllegalArgumentException::new);
    }

    @Nonnull
    public Material getMaterial() {
        return material;
    }

    @Nonnull
    public String getName() {
        return name;
    }

    @Nonnull
    public Transform getTransform() {
        return transform;
    }

    public String toString() {
        return name + ": " + transform;
    }
}

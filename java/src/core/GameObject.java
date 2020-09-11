package core;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * The game object
 */
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

    /**
     * Initialization
     */
    public void start() {

    }

    /**
     * Update the states and position
     *
     * @param elapsedTime the elapsed time
     */
    public void update(float elapsedTime) {

    }

    /**
     * Update the states and position when collided
     *
     * @param col the collided object
     */
    public void onCollisionEnter(GameObject col) {

    }

    /**
     * Update the states and position when not collided with the object
     *
     * @param col the object that not collided anymore
     */
    public void onCollisionExit(GameObject col) {

    }

    /**
     * @return the game reference
     */
    @Nullable
    public Game getGame() {
        return game;
    }

    /**
     * @param game the game reference
     */
    public void setGame(Game game) {
        this.game = Optional.ofNullable(game).orElseThrow(IllegalArgumentException::new);
    }

    /**
     * @return the material of the object
     */
    @Nonnull
    public Material getMaterial() {
        return material;
    }

    /**
     * @return the name of the object
     */
    @Nonnull
    public String getName() {
        return name;
    }

    /**
     * @return the position and shape of the object
     */
    @Nonnull
    public Transform getTransform() {
        return transform;
    }

    @Override
    public String toString() {
        return name + ": " + transform;
    }
}

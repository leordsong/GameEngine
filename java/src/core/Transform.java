package core;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * A class that represents an object's position and shape
 */
public class Transform extends Component {

    private Vector2 position;
    private Vector2 size;
    private float depth;

    public Transform() {
        position = new Vector2(0, 0);
        size = new Vector2(20, 20);
        depth = 0;
    }

    @Nonnull
    public Vector2 getPosition() {
        return position;
    }

    /**
     * Set the position of the object
     *
     * @param position the position of the object
     */
    public void setPosition(Vector2 position) {
        this.position = Optional.ofNullable(position).orElseThrow(IllegalArgumentException::new);
    }

    /**
     * Update the position of the object
     *
     * @param x the change along x axis
     * @param y the change along y axis
     */
    public void updatePosition(float x, float y) {
        position = new Vector2(position.getX() + x, position.getY() + y);
    }

    /**
     * @return the depth of the object
     */
    public float getDepth() {
        return depth;
    }

    /**
     * @param depth the depth of the object
     */
    public void setDepth(float depth) {
        this.depth = depth;
    }

    /**
     * @return the size of the object
     */
    @Nonnull
    public Vector2 getSize() {
        return size;
    }

    /**
     * @param size the size of the object
     */
    public void setSize(Vector2 size) {
        this.size = Optional.ofNullable(size).orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public String toString() {
        return position + "@" + depth;
    }

}

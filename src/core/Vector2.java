package core;

/**
 * A 2D vector
 */
public final class Vector2 {
    private final float x;
    private final float y;

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return the x axis
     */
    public float getX() {
        return x;
    }

    /**
     * @return the y axis
     */
    public float getY() {
        return y;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}

package core;

import java.awt.*;

/**
 * Object's material
 */
public class Material extends Component {
    private Color color;
    private boolean isVisible;

    public Material(Color color) {
        this.color = color;
        isVisible = true;
    }

    public Material() {
        this(Color.RED);
    }

    /**
     * @return the color of the material
     */
    public Color getColor() {
        return color;
    }

    /**
     * @param color the color of the material
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * @return whether the material is visible
     */
    public boolean isVisible() {
        return isVisible;
    }

    /**
     * @param visible whether the material is visible
     */
    public void setVisible(boolean visible) {
        isVisible = visible;
    }
}

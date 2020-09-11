package core;

import java.awt.*;

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

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }
}

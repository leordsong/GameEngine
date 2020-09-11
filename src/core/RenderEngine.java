package core;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.*;

public class RenderEngine {
    private final JFrame frame;
    private final List<GameObject> sceneGraph = new ArrayList<>();

    public RenderEngine() {
        frame = new JFrame("Engine Core");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT));
        frame.setMinimumSize(new Dimension(Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT));
        frame.add(new GamePanel());
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);

        Rectangle r = frame.getBounds();
        System.out.println("Window size (" + r.width + ", " + r.height + ")");
    }

    public void renderScene(List<GameObject> gameObjects) {
        sceneGraph.addAll(gameObjects);
        frame.repaint();
    }

    public JFrame getWindow() {
        return frame;
    }

    class GamePanel extends JPanel {
        private static final long serialVersionUID = 1L;

        @Override
        public void paintComponent(Graphics g) {
            // Iterate over each game object and render it
            for (GameObject go : sceneGraph) {
                g.setColor(go.getMaterial().getColor());
                g.fillRect(
                        (int) go.getTransform().getPosition().getX(),
                        (int) go.getTransform().getPosition().getY(),
                        (int) go.getTransform().getSize().getX(),
                        (int) go.getTransform().getSize().getY()
                );
            }
        }
    }
}

package core;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * The base class of the game
 */
public abstract class Game {

    /**
     * the width of the window
     */
    public static final int WINDOW_WIDTH = 800;
    /**
     * the height of the window
     */
    public static final int WINDOW_HEIGHT = 600;

    // We require a minimum amount of time between frames to ensure that the
    // reported elapsed time is not too small, leading to potential underflow
    // errors in game objects
    private static final long MIN_FRAME_ELAPSED_TIME = 10; // ms

    private final RenderEngine renderEngine;
    private final InputEngine inputEngine;
    private final AudioEngine audioEngine;
    private final PhysicsEngine physicsEngine;
    private final List<GameObject> gameObjects = new ArrayList<>();

    public Game() {
        // Initialize architecture
        renderEngine = new RenderEngine();
        audioEngine = new AudioEngine();
        physicsEngine = new PhysicsEngine();
        inputEngine = InputEngine.getInstance();
        inputEngine.setInputSource(renderEngine.getWindow());
    }

    /**
     * Add a game object to the game
     *
     * @param g a game object
     */
    public void addGameObject(GameObject g) {
        // provide this game object with a reference back to the game
        g.setGame(this);
        // register the game object so that events are called (start, update, ...)
        gameObjects.add(g);
    }

    /**
     * Start the game in a time frame
     */
    @SuppressWarnings({"BusyWait", "InfiniteLoopStatement"})
    public void startGame() {
        // Initialize all components by calling their Start method
        gameObjects.forEach(GameObject::start);
        long previousFrameStartTime = System.currentTimeMillis();
        while (true) {
            // Wait for a frame time
            try {
                Thread.sleep(MIN_FRAME_ELAPSED_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            float elapsedTime = (System.currentTimeMillis() - previousFrameStartTime) / 1000f;
            // Update frame time
            previousFrameStartTime = System.currentTimeMillis();

            // start the frame
            inputEngine.startFrame();

            // Update all components by running their Update method
            gameObjects.forEach((go) -> go.update(elapsedTime));

            // Check for collisions
            physicsEngine.checkCollisions(gameObjects);

            // Render the scene by drawing each component's sprite
            renderEngine.renderScene(gameObjects);
        }
    }

    /**
     * @return the audio engine for the game
     */
    @Nonnull
    public AudioEngine getAudioEngine() {
        return audioEngine;
    }
}

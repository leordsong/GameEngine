package core;

import java.awt.event.*;

import javax.swing.*;

import java.util.*;

/**
 * The input engine
 */
public class InputEngine {
    private static InputEngine inputEngine = null;

    /**
     * @return the Input component
     */
    public static InputEngine getInstance() {
        if (inputEngine == null) {
            inputEngine = new InputEngine();
        }
        return inputEngine;
    }

    private final Map<Integer, KeyChange> keys = new HashMap<>();
    private KeyEventProcessor keyEventProcessor;

    /**
     * Private constructor used to support singleton design pattern.
     */
    private InputEngine() {
    }

    /**
     * Specifies the time that the current frame started.
     */
    public void startFrame() {
        // Flag all existing statuses in dictionary as no longer referring to current frame.
        keys.forEach((key, keyChange) -> keyChange.currentFrame = false);

        // Copy all buffered events to the dictionary
        for (KeyEvent ev : keyEventProcessor.getEvents()) {
            // Special case - Java auto-repeats events when key is held down. We
            // only want to record the first one. If this is a key pressed event and
            // there is already a key pressed event in the dictionary for this key,
            // throw the event away.
            if (ev.getID() == KeyEvent.KEY_PRESSED) {
                KeyChange kc = keys.get(ev.getKeyCode());
                if (kc != null && kc.state == KeyChange.KEY_DOWN) {
                    // this is a repeat - throw away this event
                    continue;
                }
            }

            // Create a KeyChange object recording the event and add it to the dictionary
            KeyChange kc = new KeyChange();
            kc.currentFrame = true;
            if (ev.getID() == KeyEvent.KEY_PRESSED) {
                kc.state = KeyChange.KEY_DOWN;
            } else {
                assert ev.getID() == KeyEvent.KEY_RELEASED;
                kc.state = KeyChange.KEY_UP;
            }
            keys.put(ev.getKeyCode(), kc);
        }
    }

    /**
     * Add the input source to the frame
     *
     * @param jFrame the current frame
     */
    public void setInputSource(JFrame jFrame) {
        // Ensure that all inputs are captured by this component, including "traversal"
        // inputs such as the tab key.
        jFrame.setFocusTraversalKeysEnabled(false);

        // Add the listener for key events
        keyEventProcessor = new KeyEventProcessor();
        jFrame.addKeyListener(keyEventProcessor);
    }

    /**
     * @return {@code true} if this key is currently depressed, {@code false} otherwise
     */
    public boolean getKey(int key) {
        return keys.containsKey(key) && keys.get(key).state == KeyChange.KEY_DOWN;
    }

    /**
     * @return {@code true} if this key has been depressed in the last frame, {@code false} otherwise
     */
    public boolean getKeyDown(int key) {
        if (keys.containsKey(key)) {
            KeyChange kc = keys.get(key);
            return kc.state == KeyChange.KEY_DOWN && kc.currentFrame;
        }
        return false;
    }

    /**
     * @return {@code true} if this key has been released in the last frame, {@code false} otherwise
     */
    public boolean getKeyUp(int key) {
        if (keys.containsKey(key)) {
            KeyChange kc = keys.get(key);
            return kc.state == KeyChange.KEY_UP && kc.currentFrame;
        }
        return false;
    }

    // Adding mouse input is not required for this assignment, but is an optional challenge for no extra credit
    public boolean getMouseButton(int button) {
        return false;
    }

    public boolean getMouseButtonDown(int button) {
        return false;
    }

    public boolean getMouseButtonUp(int button) {
        return false;
    }

    public Vector2 getMouse() {
        return new Vector2(0, 0);
    }

    private static class KeyChange {
        public static final int KEY_UP = 0;
        public static final int KEY_DOWN = 1;

        public int state;    // KEY_UP or KEY_DOWN
        public boolean currentFrame;    // true if this change was in the current frame
    }

    /**
     * Class to deal with key events.
     * Events are buffered, and returned in one chunk
     * when requested by the getEvents method.
     */
    private static class KeyEventProcessor implements KeyListener {
        private final List<KeyEvent> events = new ArrayList<>();

        @Override
        public synchronized void keyPressed(KeyEvent e) {
            events.add(e);
        }

        @Override
        public synchronized void keyReleased(KeyEvent e) {
            events.add(e);
        }

        @Override
        public synchronized void keyTyped(KeyEvent e) {

        }

        /**
         * Returns all new keyboard events since the last time this method
         * was called.
         *
         * @return list of new keyboard events
         */
        public synchronized List<KeyEvent> getEvents() {
            List<KeyEvent> theEvents = new ArrayList<>(events);
            events.clear();
            return theEvents;
        }
    }

}
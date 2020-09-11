package core;

import javax.sound.sampled.*;
import java.io.File;
import java.util.*;

/**
 * The audio engine
 */
public class AudioEngine {
    private final Map<String, String> AUDIO_CLIPS = new HashMap<>();

    /**
     * Add flip to the list
     *
     * @param clipName     the name of the clip
     * @param clipFileName the path to the audio file
     */
    public void addClip(String clipName, String clipFileName) {
        AUDIO_CLIPS.put(clipName, clipFileName);
    }

    /**
     * Play the audio by name
     *
     * @param clipName the name of the audio clip
     */
    public void playOneShot(String clipName) {
        String clipFileName = AUDIO_CLIPS.get(clipName);
        play(clipFileName);
    }

    private synchronized void play(final String fileName) {
        // This code adapted from http://noobtuts.com/java/play-sounds
        // Note: use .wav files             
        new Thread(() -> {
            try {
                Clip clip = AudioSystem.getClip();
                AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(fileName));
                clip.open(inputStream);
                clip.start();
            } catch (Exception e) {
                System.err.println("play sound error: " + e.getMessage() + " for " + fileName);
            }
        }).start();
    }
}

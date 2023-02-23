package danogl.gui;

import javax.sound.sampled.*;
import java.io.IOException;

/**
 * Class for reading Sounds from the disk or from within a jar
 * @author Dan Nirel
 */
public class SoundReader {
    private static final int MAX_SIMULTANEOUS_RUNS = 5;
    private WindowController windowController;
    private ResourceReader resReader = new ResourceReader();

    /** Used by the library */
    public SoundReader(WindowController windowController) {
        this.windowController = windowController;
    }

    /** Read the sound with the specified path from disk or from within the jar */
    public Sound readSound(String wavFilePath) {
        Clip[] clips = new Clip[MAX_SIMULTANEOUS_RUNS];
        for (int i = 0; i < clips.length ; i++) {
            try (var stream = resReader.readResource(wavFilePath)) {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(stream);
                clips[i] = AudioSystem.getClip();
                clips[i].open(audioInputStream);
            } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
                windowController.showMessageBox(
                        String.format("Failed to open file '%s': %s", wavFilePath, e.getMessage()));
                windowController.closeWindow();
            }
        }
        return new Sound(clips);
    }
}
